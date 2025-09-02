package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pr1nkos.taskmanager.constant.TaskStatus;
import ru.pr1nkos.taskmanager.dto.request.CreateSubtaskRequest;
import ru.pr1nkos.taskmanager.entity.Subtask;
import ru.pr1nkos.taskmanager.entity.Task;
import ru.pr1nkos.taskmanager.exception.ResourceNotFoundException;
import ru.pr1nkos.taskmanager.repository.SubtaskRepository;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {

    private final TaskService taskService;
    private final SubtaskRepository subtaskRepository;

    @Override
    public Page<Subtask> getSubtasksByAssignee(Long memberId, Pageable pageable) {
        return subtaskRepository.findByAssignedToMemberId(memberId, pageable);
    }

    @Override
    public Subtask getSubtaskByIdAndAssignee(Long id, Long memberId) {
        return subtaskRepository.findByIdAndAssignedToMemberId(id, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found"));
    }

    @Override
    public Subtask createSubtask(CreateSubtaskRequest request, Long creatorId) {
//        Member assignee = memberService.findById(request.assignToMemberId()); //TODO Сделать проверку, что такой мембер есть
        Task task = taskService.getTaskById(request.taskId()); //TODO Делать проверку, что такой таск есть
        Subtask subtask = Subtask.builder()
                .title(request.title())
                .taskId(request.taskId())
                .description(request.description())
                .assignedToMemberId(request.assignToMemberId())
                .status(TaskStatus.TODO)
                .build();
        task.getSubtasks().add(subtask);

        subtaskRepository.save(subtask);
        return subtask;
    }

    @Override
    public Subtask reassignSubtask(Long subtaskId, Long newAssigneeId, Long requesterId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found"));

        subtask.setAssignedToMemberId(newAssigneeId);
        return subtaskRepository.save(subtask);
    }

    @Override
    public Subtask updateStatus(Long id, TaskStatus status, Long memberId) {
        Subtask subtask = subtaskRepository.findByIdAndAssignedToMemberId(id, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found or not assigned to you"));

        subtask.setStatus(status);
        return subtaskRepository.save(subtask);
    }

    @Override
    public void save(Subtask subtask) {
        subtaskRepository.save(subtask);
    }
}
