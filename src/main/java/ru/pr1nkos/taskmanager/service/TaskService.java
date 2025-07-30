package ru.pr1nkos.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pr1nkos.taskmanager.entity.Task;
import ru.pr1nkos.taskmanager.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    @Transactional(readOnly = true)
    public Page<Task> getTasks(Long memberId, Pageable pageable) {
        return taskRepository.findTasksByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Task> getListOfTasks(Long memberId) {
        return taskRepository.findTasksByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id).orElse(null);
    }

    @Transactional
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
