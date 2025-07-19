package ru.pr1nkos.taskmanager.controller;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pr1nkos.taskmanager.config.JwtTokenUtil;
import ru.pr1nkos.taskmanager.dto.request.LoginRequest;
import ru.pr1nkos.taskmanager.dto.request.RegisterRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.service.MemberService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Member member = memberService.findByUsername(loginRequest.username());
        Assert.notNull(member);
        boolean isCredentialsCorrect = memberService.authenticate(loginRequest);
        if (!isCredentialsCorrect) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        String token = jwtTokenUtil.generateToken(loginRequest.username());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String password = passwordEncoder.encode(registerRequest.password());
        Member member = Member.builder()
                .password(password)
                .username(registerRequest.username())
                .build();
        memberService.save(member);
        return ResponseEntity.ok("User registered successfully");
    }
}
