package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.exception.ResourceNotFoundException;
import ru.pr1nkos.taskmanager.exception.UnauthorizedProjectAccessException;
import ru.pr1nkos.taskmanager.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Project> getProjects(Long memberId, Pageable pageable) {
        return projectRepository.findProjectsByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findProjectById(id).orElse(null);
    }
    @Transactional(readOnly = true)
    @Override
    public Project getProjectByName(String name) {
        return projectRepository.findProjectByName(name).orElse(null);
    }

    @Override
    public boolean projectExists(String name) {
        return projectRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public Project getProjectByIdForUser(Long projectId, Long memberId) {
        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
        if (!project.getMemberId().equals(memberId)) {
            throw new UnauthorizedProjectAccessException("You don't have access to this project");
        }
        return project;
    }

    @Transactional
    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}