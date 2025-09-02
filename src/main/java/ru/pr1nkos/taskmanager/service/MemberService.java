package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

}
