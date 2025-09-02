package ru.pr1nkos.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.taskmanager.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskById(Long id);
    Page<Task> findTasksByProjectId(Long projectId,  Pageable pageable);
    List<Task> findTasksByMemberId(Long memberId);

    @Query("SELECT DISTINCT t.memberId FROM Task t WHERE t.projectId = :projectId AND t.memberId IS NOT NULL")
    Set<Long> findAssignedMemberIdsByProjectId(@Param("projectId") Long projectId);
}
