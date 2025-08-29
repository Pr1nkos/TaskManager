package ru.pr1nkos.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pr1nkos.taskmanager.dto.request.CreateNewProjectRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateProjectRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.exception.DuplicateProjectNameException;
import ru.pr1nkos.taskmanager.exception.ResourceNotFoundException;
import ru.pr1nkos.taskmanager.service.MemberService;
import ru.pr1nkos.taskmanager.service.ProjectService;

import java.util.Set;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final MemberService memberService;
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<Project>> getProjectsByMember(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        Member member = getMemberFromJwt(jwt);
        return ResponseEntity.ok(projectService.getProjects(member.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        Project project = projectService.getProjectById(id);
        validateProjectAccess(id, member);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<Project> createNewProject(@AuthenticationPrincipal Jwt jwt,
                                                    @RequestBody CreateNewProjectRequest request) {
        Member member = getMemberFromJwt(jwt);
        if (projectService.projectExists(request.name())) {
            throw new DuplicateProjectNameException("Project already exists with name: " + request.name());
        }
        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .memberId(member.getId())
                .build();
        projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable Long id,
                                                 @RequestBody UpdateProjectRequest request) {
        Member member = getMemberFromJwt(jwt);
        Project project = projectService.getProjectById(id);
        validateProjectAccess(id, member);
        project.setName(request.name());
        project.setDescription(request.description());
        projectService.saveProject(project);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Member member = getMemberFromJwt(jwt);
        validateProjectAccess(id, member);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/assigned-members")
    public ResponseEntity<Set<Long>> getAssignedMemberIds(@PathVariable("projectId") Long projectId,
                                                          @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        validateProjectAccess(projectId, member);

        Set<Long> assignedMemberIds = projectService.getAssignedMemberIds(projectId);
        return ResponseEntity.ok(assignedMemberIds);
    }

    private Member getMemberFromJwt(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return memberService.findByUsername(username);
    }

    private void validateProjectAccess(Long projectId, Member member) {
        Project project = projectService.getProjectById(projectId);
        if (project == null || !member.getId().equals(project.getMemberId())) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectId);
        }
    }
}