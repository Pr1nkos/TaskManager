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
import ru.pr1nkos.taskmanager.constant.TaskStatus;
import ru.pr1nkos.taskmanager.dto.request.AssignSubtaskRequest;
import ru.pr1nkos.taskmanager.dto.request.CreateSubtaskRequest;
import ru.pr1nkos.taskmanager.entity.Member;
import ru.pr1nkos.taskmanager.entity.Subtask;
import ru.pr1nkos.taskmanager.service.MemberService;
import ru.pr1nkos.taskmanager.service.SubtaskService;

@RestController
@RequestMapping("/api/subtasks")
@RequiredArgsConstructor
@Slf4j
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final MemberService memberService;

    private Member getMemberFromJwt(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return memberService.findByUsername(username);
    }

    // Получить все подзадачи, назначенные мне
    @GetMapping
    public ResponseEntity<Page<Subtask>> getMySubtasks(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Member member = getMemberFromJwt(jwt);
        Page<Subtask> subtasks = subtaskService.getSubtasksByAssignee(member.getId(), pageable);
        return ResponseEntity.ok(subtasks);
    }

    // Получить подзадачу по ID (с проверкой доступа)
    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getSubtask(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        Subtask subtask = subtaskService.getSubtaskByIdAndAssignee(id, member.getId());
        return ResponseEntity.ok(subtask);
    }

    // Создать подзадачу (вручную, например, для админа)
    @PostMapping
    public ResponseEntity<Subtask> createSubtask(
            @RequestBody CreateSubtaskRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        Subtask subtask = subtaskService.createSubtask(request, member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subtask);
    }

    // Переназначить подзадачу
    @PutMapping("/{id}/assign")
    public ResponseEntity<Subtask> assignSubtask(
            @PathVariable Long id,
            @RequestBody AssignSubtaskRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        Subtask updated = subtaskService.reassignSubtask(id, request.newAssigneeId(), member.getId());
        return ResponseEntity.ok(updated);
    }

    // Обновить статус подзадачи (только исполнитель)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Subtask> updateStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status,
            @AuthenticationPrincipal Jwt jwt) {
        Member member = getMemberFromJwt(jwt);
        Subtask subtask = subtaskService.updateStatus(id, status, member.getId());
        return ResponseEntity.ok(subtask);
    }
}
