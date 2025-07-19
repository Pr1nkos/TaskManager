package ru.pr1nkos.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.taskmanager.entity.Member;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);
}
