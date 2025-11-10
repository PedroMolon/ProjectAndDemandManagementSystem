package com.pedromolon.demoprojectanddemandmanagementsystem.service;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskStatusRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Task;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.User;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import com.pedromolon.demoprojectanddemandmanagementsystem.mapper.TaskMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.ProjectRepository;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    Status status = Status.DOING;
    Priority priority = Priority.HIGH;
    LocalDate dueDate = LocalDate.of(2025, 10, 1);

    @Test
    void quandoCriarTarefa_deveRetornarSucesso() {
        User user = new User();
        user.setId(1L);
        user.setName("Pedro");

        TaskRequest request = new TaskRequest(1L, "Tarefa Teste", "Descrição", status, priority, dueDate);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        Task saveTask = new Task();
        saveTask.setId(99L);

        TaskResponse response = new TaskResponse(
                99L,  1L,"Tarefa Teste", "Descrição", status, priority, dueDate, 1L, "Pedro"
        );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(saveTask);
        when(taskMapper.toResponse(saveTask)).thenReturn(response);

        TaskResponse taskResponse = taskService.createTask(user, request);

        assertNotNull(response);
        assertEquals(taskResponse, response);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void quandoBuscarTarefasComFiltros_deveRetornarPaginaDeTarefas() {
        Status filterStatus = Status.DOING;
        Priority filterPriority = Priority.HIGH;
        Long filterProjectId = 1L;
        Pageable pageable = PageRequest.of(0, 5);

        Task foundTask = new Task();
        foundTask.setId(100L);
        foundTask.setTitle("Tarefa");
        foundTask.setDescription("Descrição");
        foundTask.setStatus(filterStatus);
        foundTask.setPriority(filterPriority);
        foundTask.setDueDate(dueDate);

        List<Task> tasks = List.of(foundTask);

        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        TaskResponse response = new TaskResponse(
                100L, 1L, "Tarefa", "Descrição", filterStatus, filterPriority,
                dueDate, 1L, "Usuário"
        );

        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);
        when(taskMapper.toResponse(foundTask)).thenReturn(response);

        Page<TaskResponse> taskResponses = taskService.findTaskWithFilters(filterStatus, filterPriority, filterProjectId, pageable);

        assertNotNull(taskResponses);
        assertEquals(1, taskPage.getTotalElements());
        assertEquals(response, taskResponses.getContent().getFirst());

        verify(taskRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void quandoAtualizarStatusTarefa_deveRetornarTarefaAtualizada() {
        Long taskId = 1L;

        TaskStatusRequest request = new TaskStatusRequest(Status.DONE);

        Task foundTask = new Task();
        foundTask.setId(taskId);
        foundTask.setTitle("Tarefa");
        foundTask.setStatus(Status.TODO);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Tarefa");
        updatedTask.setStatus(Status.DONE);

        TaskResponse response = new TaskResponse(
                taskId, null, "Tarefa", null, Status.DONE, null, null, null, null
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(foundTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toResponse(updatedTask)).thenReturn(response);

        TaskResponse taskResponse = taskService.updateTaskStatus(taskId, request);

        assertNotNull(taskResponse);
        assertEquals(Status.DONE, taskResponse.status());
        assertEquals(response, taskResponse);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void quandoDeletarTarefa_deveRetornarSucesso() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

}
