package ru.pr1nkos.taskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.pr1nkos.taskmanager.dto.request.CreateTaskRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateTaskRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.entity.Task;
import ru.pr1nkos.taskmanager.service.MemberService;
import ru.pr1nkos.taskmanager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final MemberService memberService;
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<Task>> getTasksByProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long projectId,
            Pageable pageable) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Page<Task> tasks = taskService.getTasksForUser(projectId, member.getId(), pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Task task = taskService.getTaskByIdForUser(id, member.getId());
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateTaskRequest request) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Task task = taskService.createTaskForUser(member.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody @Valid UpdateTaskRequest request) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        Task task = taskService.updateTaskForUser(id, member.getId(), request);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        String username = userDetails.getUsername();
        Member member = memberService.findByUsername(username);
        taskService.deleteTaskForUser(id, member.getId());
        return ResponseEntity.noContent().build();
    }
}