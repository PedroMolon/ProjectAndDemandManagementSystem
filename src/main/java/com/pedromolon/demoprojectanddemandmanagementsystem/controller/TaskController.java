package com.pedromolon.demoprojectanddemandmanagementsystem.controller;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskStatusRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import com.pedromolon.demoprojectanddemandmanagementsystem.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<TaskResponse>> getTasks (
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long projectId,
            @PageableDefault(size = 10, sort = "dueDate", direction = Sort.Direction.ASC)Pageable pageable) {
        Page<TaskResponse> tasks = taskService.findTaskWithFilters(
                status,
                priority,
                projectId,
                pageable
        );

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId, @Valid @RequestBody TaskStatusRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTaskStatus(taskId, request));
    }

}
