package ru.pr1nkos.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pr1nkos.taskmanager.dto.request.CreateTaskRequest;
import ru.pr1nkos.taskmanager.dto.request.UpdateTaskRequest;
import ru.pr1nkos.taskmanager.entity.Task;

import java.util.List;

public interface TaskService {
    Page<Task> getTasks(Long projectId, Pageable pageable);
    List<Task> getListOfTasks(Long memberId);
    Task getTaskById(Long id);
    void saveTask(Task task);
    void deleteTask(Long id);

    Page<Task> getTasksForUser(Long projectId, Long memberId, Pageable pageable);
    Task getTaskByIdForUser(Long taskId, Long memberId);
    Task createTaskForUser(Long memberId, CreateTaskRequest request);
    Task updateTaskForUser(Long taskId, Long memberId, UpdateTaskRequest request);
    void deleteTaskForUser(Long taskId, Long memberId);
}