package ru.pr1nkos.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pr1nkos.taskmanager.entity.Project;

import java.util.Set;

public interface ProjectService {
    Project getProjectById(Long id);
    Project getProjectByName(String name);
    boolean projectExists(String name);
    Page<Project> getProjects(Long memberId, Pageable pageable);
    void saveProject(Project project);
    void deleteProject(Long id);
    Project getProjectByIdForUser(Long projectId, Long memberId);
    Set<Long> getAssignedMemberIds(Long projectId);
}
