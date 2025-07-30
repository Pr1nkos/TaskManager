package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.dto.request.LoginRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean authenticate(LoginRequest loginRequest) {
        return memberRepository.findByUsername(loginRequest.username())
                .map(member -> passwordEncoder.matches(loginRequest.password(), member.getPassword()))
                .orElse(false);
    }
}
