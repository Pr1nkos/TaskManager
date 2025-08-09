package ru.pr1nkos.taskmanager.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.pr1nkos.taskmanager.client.KeycloakClient;
import ru.pr1nkos.taskmanager.dto.request.LoginRequest;
import ru.pr1nkos.taskmanager.dto.request.RegisterRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.service.MemberService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final KeycloakClient keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;
    @Value("${keycloak.admin-client-id}")
    private String adminClientId;
    @Value("${keycloak.admin-username}")
    private String adminUsername;
    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String EMAIL_KEY = "email";
    private static final String ENABLED_KEY = "enabled";
    private static final String CREDENTIALS_KEY = "credentials";
    private static final String TYPE_KEY = "type";
    private static final String VALUE_KEY = "value";
    private static final String TEMPORARY_KEY = "temporary";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DEFAULT_EMAIL_SUFFIX = "@example.com";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(GRANT_TYPE_KEY, GRANT_TYPE_PASSWORD);
            formData.add(CLIENT_ID_KEY, clientId);
            formData.add(CLIENT_SECRET_KEY, clientSecret);
            formData.add(USERNAME_KEY, loginRequest.username());
            formData.add(PASSWORD_KEY, loginRequest.password());

            Map<String, Object> response = keycloakClient.getToken(realm, formData);

            return ResponseEntity.ok(response);

        } catch (FeignException e) {
            return handleKeycloakError(e);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    ERROR_KEY, "Internal server error",
                    MESSAGE_KEY, e.getMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String adminToken = getAdminToken();

            Map<String, Object> userRepresentation = Map.of(
                    USERNAME_KEY, registerRequest.username(),
                    EMAIL_KEY, registerRequest.email() != null ? registerRequest.email() : registerRequest.username() + DEFAULT_EMAIL_SUFFIX,
                    ENABLED_KEY, true,
                    CREDENTIALS_KEY, new Object[]{
                            Map.of(
                                    TYPE_KEY, GRANT_TYPE_PASSWORD,
                                    VALUE_KEY, registerRequest.password(),
                                    TEMPORARY_KEY, false
                            )
                    }
            );
            keycloakClient.createUser(realm, BEARER_PREFIX + adminToken, userRepresentation);

            saveUserToLocalDatabase(registerRequest);

            return ResponseEntity.ok(Map.of(
                    MESSAGE_KEY, "User registered successfully",
                    USERNAME_KEY, registerRequest.username()
            ));

        } catch (FeignException e) {
            return handleKeycloakError(e);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    ERROR_KEY, "Internal server error",
                    MESSAGE_KEY, e.getMessage()
            ));
        }
    }

    private String getAdminToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(GRANT_TYPE_KEY, GRANT_TYPE_PASSWORD);
        formData.add(CLIENT_ID_KEY, adminClientId);
        formData.add(USERNAME_KEY, adminUsername);
        formData.add(PASSWORD_KEY, adminPassword);

        Map<String, Object> response = keycloakClient.getToken("master", formData);
        return (String) response.get(ACCESS_TOKEN_KEY);
    }

    private void saveUserToLocalDatabase(RegisterRequest registerRequest) {
        try {
            Member member = Member.builder()
                    .username(registerRequest.username())
                    .email(registerRequest.email())
                    .build();
            memberService.save(member);
        } catch (Exception e) {
            log.error("Failed to save user to local database: " + e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Object>> handleKeycloakError(FeignException e) {
        int status = e.status();
        return switch (status) {
            case 401 -> ResponseEntity.status(401).body(Map.of(
                    ERROR_KEY, "Invalid credentials",
                    MESSAGE_KEY, "Username or password is incorrect"
            ));
            case 400 -> ResponseEntity.status(400).body(Map.of(
                    ERROR_KEY, "Bad request",
                    MESSAGE_KEY, "Account may not be fully set up"
            ));
            case 409 -> ResponseEntity.status(409).body(Map.of(
                    ERROR_KEY, "User already exists",
                    MESSAGE_KEY, "Username or email already taken"
            ));
            default -> ResponseEntity.status(status < 0 ? 500 : status).body(Map.of(
                    ERROR_KEY, "Authentication failed",
                    MESSAGE_KEY, e.getMessage()
            ));
        };
    }
}