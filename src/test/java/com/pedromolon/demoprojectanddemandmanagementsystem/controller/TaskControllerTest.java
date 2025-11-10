package com.pedromolon.demoprojectanddemandmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskStatusRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.User;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import com.pedromolon.demoprojectanddemandmanagementsystem.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser
    void quandoCriarTarefa_deveRetornarSucesso() throws Exception {

        TaskRequest request = new TaskRequest(
                1L, "Tarefa", "Descrição", Status.DOING, Priority.LOW, LocalDate.of(2025, 12, 31)
        );

        TaskResponse response = new TaskResponse(
                10L,
                1L,
                "Tarefa",
                "Descrição",
                Status.DOING,
                Priority.LOW,
                LocalDate.of(2025, 12, 31),
                1L,
                "Pedro"
        );

        when(taskService.createTask(nullable(User.class), any(TaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tarefa"))
                .andExpect(jsonPath("$.status").value("DOING"))
                .andExpect(jsonPath("$.projectId").value(1L));
    }

    @Test
    @WithMockUser
    void quandoBuscarTarefasComFiltros_deveRetornarPaginaDeTarefas() throws Exception {
        Status status = Status.DOING;
        Priority priority = Priority.HIGH;
        Long projectId = 1L;

        TaskResponse response = new TaskResponse(
                20L,
                projectId,
                "Tarefa",
                "Descrição",
                status,
                priority,
                LocalDate.of(2025,11, 15),
                1L,
                "Pedro"
        );

        Page<TaskResponse> taskResponses = new PageImpl<>(
                List.of(response),
                PageRequest.of(0, 10),
                1
        );

        when(taskService.findTaskWithFilters(eq(status), eq(priority), eq(projectId), any(Pageable.class)))
                .thenReturn(taskResponses);

        mockMvc.perform(
                get("/tasks")
                        .param("status", status.name())
                        .param("priority", priority.name())
                        .param("projectId", String.valueOf(projectId))
                        .param("page", "0")
                        .param("size", "10")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Tarefa"))
                .andExpect(jsonPath("$.content[0].status").value(status.name()));
    }

    @Test
    @WithMockUser
    void quandoAtualizarStatusTarefa_deveRetornarTarefaAtualizada() throws Exception {
        Long taskId = 1L;
        TaskStatusRequest novoStatusRequest = new TaskStatusRequest(Status.DONE);

        TaskResponse tarefaAtualizadaResponse = new TaskResponse(
                taskId, 1L, "Tarefa Atualizada", "Descrição",
                Status.DONE, Priority.MEDIUM, LocalDate.of(2025, 11, 15),
                1L, "Pedro"
        );

        when(taskService.updateTaskStatus(eq(taskId), any(TaskStatusRequest.class)))
                .thenReturn(tarefaAtualizadaResponse);

        mockMvc.perform(
                put("/tasks/{taskId}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoStatusRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.status").value(Status.DONE.name())); // Verifica o novo status
    }

    @Test
    @WithMockUser
    void quandoDeletarTarefa_deveRetornarSucesso() throws Exception {
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(
                delete("/tasks/{taskId}", taskId)
        )
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(eq(taskId));
    }

}
