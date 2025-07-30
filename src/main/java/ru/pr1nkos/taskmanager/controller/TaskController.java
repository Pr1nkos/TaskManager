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
import ru.pr1nkos.taskmanager.constant.TaskStatus;
import ru.pr1nkos.taskmanager.dto.request.CreateTaskRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateTaskRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.entity.Task;
import ru.pr1nkos.taskmanager.exception.UnauthorizedTaskAccessException;
import ru.pr1nkos.taskmanager.service.MemberService;
import ru.pr1nkos.taskmanager.service.ProjectService;
import ru.pr1nkos.taskmanager.service.TaskService;
import ru.pr1nkos.taskmanager.util.ErrorResponseUtils;

import java.util.Objects;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final MemberService memberService;
    private final ProjectService projectService;
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Object> getTasksByProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long projectId,
            Pageable pageable) {

        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Project project = projectService.getProjectById(projectId);

        if (project == null) {
            return ErrorResponseUtils.notFound("Project not found");
        }

        if (!Objects.equals(project.getMemberId(), member.getId())) {
            throw new UnauthorizedTaskAccessException("You don't have access to tasks in this project");
        }

        Page<Task> tasks = taskService.getTasks(projectId, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);

        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ErrorResponseUtils.notFound("Task not found");
        }

        Project project = projectService.getProjectById(task.getProjectId());
        if (project == null) {
            return ErrorResponseUtils.notFound("Associated project not found");
        }

        if (!Objects.equals(project.getMemberId(), member.getId())) {
            throw new UnauthorizedTaskAccessException("You don't have access to this task");
        }

        return ResponseEntity.ok(task);
    }


    @PostMapping
    public ResponseEntity<Object> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateTaskRequest request) {

        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);

        Project project = projectService.getProjectById(request.projectId());
        if (project == null) {
            return ErrorResponseUtils.notFound("Project not found");
        }

        if (!Objects.equals(project.getMemberId(), member.getId())) {
            throw new UnauthorizedTaskAccessException("You can't add tasks to this project");
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .projectId(request.projectId())
                .memberId(member.getId())
                .build();

        taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request) {

        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);

        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ErrorResponseUtils.notFound("Task not found");
        }

        Project project = projectService.getProjectById(task.getProjectId());
        if (project == null) {
            return ErrorResponseUtils.notFound("Associated project not found");
        }

        if (!Objects.equals(project.getMemberId(), member.getId())) {
            throw new UnauthorizedTaskAccessException("You can't modify tasks in this project");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        taskService.saveTask(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);

        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ErrorResponseUtils.notFound("Task not found");
        }

        Project project = projectService.getProjectById(task.getProjectId());
        if (project == null) {
            return ErrorResponseUtils.notFound("Associated project not found");
        }

        if (!Objects.equals(project.getMemberId(), member.getId())) {
            throw new UnauthorizedTaskAccessException("You can't delete tasks from this project");
        }

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}