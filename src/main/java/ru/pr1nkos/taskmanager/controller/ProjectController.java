package ru.pr1nkos.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.pr1nkos.taskmanager.dto.request.CreateNewProjectRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateProjectRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.exception.ResourceNotFoundException;
import ru.pr1nkos.taskmanager.exception.UnauthorizedProjectAccessException;
import ru.pr1nkos.taskmanager.service.MemberService;
import ru.pr1nkos.taskmanager.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final MemberService memberService;
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<Project>> getProjectsByMember(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        return ResponseEntity.ok(projectService.getProjects(member.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Project project = projectService.getProjectById(id);
        if (project == null || !member.getId().equals(project.getMemberId())) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<Project> createNewProject(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody CreateNewProjectRequest createNewProjectRequest) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Project project = Project.builder()
                .name(createNewProjectRequest.name())
                .description(createNewProjectRequest.description())
                .memberId(member.getId())
                .build();
        projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id,
                                                 @RequestBody UpdateProjectRequest updateProjectRequest) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Project project = projectService.getProjectById(id);

        if (project == null || !member.getId().equals(project.getMemberId())) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }

        if (!member.getId().equals(project.getMemberId())) {
            throw new UnauthorizedProjectAccessException("You cannot update not your project");
        }

        project.setName(updateProjectRequest.name());
        project.setDescription(updateProjectRequest.description());
        projectService.saveProject(project);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Project project = projectService.getProjectById(id);

        if (project == null || !member.getId().equals(project.getMemberId())) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }

        if (!member.getId().equals(project.getMemberId())) {
            throw new UnauthorizedProjectAccessException("You cannot delete not your project");
        }
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

