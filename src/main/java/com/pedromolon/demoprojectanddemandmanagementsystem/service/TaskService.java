package com.pedromolon.demoprojectanddemandmanagementsystem.service;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskStatusRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Task;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.specification.TaskSpecification;
import com.pedromolon.demoprojectanddemandmanagementsystem.mapper.TaskMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.ProjectRepository;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found for id: " + request.projectId()));

        Task task = taskMapper.toEntity(request);
        task.setProject(project);

        return taskMapper.toResponse(
                taskRepository.save(task)
        );
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findTaskWithFilters(
            Status status,
            Priority priority,
            Long projectId,
            Pageable pageable
    ) {
        Specification<Task> spec = TaskSpecification.filterTasks(status, priority, projectId);

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return taskPage.map(taskMapper::toResponse);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, TaskStatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found for id: " + taskId));

        task.setStatus(request.status());

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found for id: " + taskId);
        }

        taskRepository.deleteById(taskId);
    }

}
