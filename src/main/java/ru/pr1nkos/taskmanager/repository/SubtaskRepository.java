package ru.pr1nkos.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.taskmanager.entity.Subtask;

import java.util.Optional;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    Page<Subtask> findByAssignedToMemberId(Long memberId, Pageable pageable);
    Optional<Subtask> findByIdAndAssignedToMemberId(Long id, Long memberId);
}
