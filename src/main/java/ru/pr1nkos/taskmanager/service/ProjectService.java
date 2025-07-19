package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Page<Project> getProjects(Long memberId, Pageable pageable) {
        return projectRepository.findProjectsByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Project> getListOfProjects(Long memberId) {
        return projectRepository.findProjectsByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findProjectById(id).orElse(null);
    }

    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
