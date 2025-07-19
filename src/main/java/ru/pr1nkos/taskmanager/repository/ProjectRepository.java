package ru.pr1nkos.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pr1nkos.taskmanager.entity.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findProjectsByMemberId(Long memberId, Pageable pageable);
    List<Project> findProjectsByMemberId(Long memberId);
    Optional<Project> findProjectById(Long projectId);
}
