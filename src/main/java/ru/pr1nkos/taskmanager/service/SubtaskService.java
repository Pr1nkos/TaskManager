package ru.pr1nkos.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pr1nkos.taskmanager.constant.TaskStatus;
import ru.pr1nkos.taskmanager.dto.request.CreateSubtaskRequest;
import ru.pr1nkos.taskmanager.entity.Subtask;

public interface SubtaskService {
    Page<Subtask> getSubtasksByAssignee(Long memberId, Pageable pageable);
    Subtask getSubtaskByIdAndAssignee(Long id, Long memberId);
    Subtask createSubtask(CreateSubtaskRequest request, Long creatorId);
    Subtask reassignSubtask(Long subtaskId, Long newAssigneeId, Long requesterId);
    Subtask updateStatus(Long id, TaskStatus status, Long memberId);
    void save(Subtask subtask);
}
