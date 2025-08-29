package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.constant.TaskStatus;
import ru.pr1nkos.taskmanager.dto.request.CreateTaskRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateTaskRequest;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.entity.Task;
import ru.pr1nkos.taskmanager.exception.ResourceNotFoundException;
import ru.pr1nkos.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectServiceImpl projectService;

    @Transactional(readOnly = true)
    @Override
    public Page<Task> getTasks(Long projectId, Pageable pageable) {
        return taskRepository.findTasksByProjectId(projectId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getListOfTasks(Long memberId) {
        return taskRepository.findTasksByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id).orElse(null);
    }

    @Transactional
    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Task> getTasksForUser(Long projectId, Long memberId, Pageable pageable) {
        projectService.getProjectByIdForUser(projectId, memberId);
        return taskRepository.findTasksByProjectId(projectId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Task getTaskByIdForUser(Long taskId, Long memberId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        projectService.getProjectByIdForUser(task.getProjectId(), memberId);
        return task;
    }

    @Transactional
    @Override
    public Task createTaskForUser(Long memberId, CreateTaskRequest request) {
        Project project = projectService.getProjectByIdForUser(request.projectId(), memberId);
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .projectId(project.getId())
                .memberId(memberId)
                .build();
        taskRepository.save(task);
        return task;
    }

    @Transactional
    @Override
    public Task updateTaskForUser(Long taskId, Long memberId, UpdateTaskRequest request) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        projectService.getProjectByIdForUser(task.getProjectId(), memberId);
        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        taskRepository.save(task);
        return task;
    }

    @Transactional
    @Override
    public void deleteTaskForUser(Long taskId, Long memberId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        projectService.getProjectByIdForUser(task.getProjectId(), memberId);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    @Override
    public Set<Long> findAssignedMemberIdsByProjectId(Long projectId) {
        return taskRepository.findAssignedMemberIdsByProjectId(projectId);
    }
}