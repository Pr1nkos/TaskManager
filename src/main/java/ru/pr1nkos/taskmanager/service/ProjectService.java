package ru.pr1nkos.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pr1nkos.taskmanager.entity.Project;

import java.util.List;

public interface ProjectService {
    Project getProjectById(Long id);
    Page<Project> getProjects(Long memberId, Pageable pageable);
    void saveProject(Project project);
    void deleteProject(Long id);
    List<Project> getListOfProjects(Long memberId);
    Project getProjectByIdForUser(Long projectId, Long memberId);
}
