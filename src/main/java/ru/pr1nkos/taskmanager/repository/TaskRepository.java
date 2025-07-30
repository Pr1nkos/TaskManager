package ru.pr1nkos.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.taskmanager.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findTasksByMemberId(Long memberId, Pageable pageable);

    List<Task> findTasksByMemberId(Long memberId);

    Optional<Task> findTaskById(Long id);
}
