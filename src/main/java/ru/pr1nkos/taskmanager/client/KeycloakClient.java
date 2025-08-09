package ru.pr1nkos.taskmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.url}")
public interface KeycloakClient {

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getToken(@PathVariable("realm") String realm, MultiValueMap<String, String> formData);

    @PostMapping(value = "/admin/realms/{realm}/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createUser(@PathVariable("realm") String realm, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, Map<String, Object> user);
}
