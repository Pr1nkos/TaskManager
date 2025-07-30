package ru.pr1nkos.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pr1nkos.taskmanager.entity.Project;

public interface ProjectService {
    Project getProjectById(Long id);
    Page<Project> getProjects(Long memberId, Pageable pageable);
    void saveProject(Project project);
    void deleteProject(Long id);
    Project getProjectByIdForUser(Long projectId, Long memberId);
}
