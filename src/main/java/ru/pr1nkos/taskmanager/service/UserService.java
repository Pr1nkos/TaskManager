package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(Member member) {
        userRepository.save(member);
    }

    public Member findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    }
}
