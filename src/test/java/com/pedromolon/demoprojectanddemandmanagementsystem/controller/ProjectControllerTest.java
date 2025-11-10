package com.pedromolon.demoprojectanddemandmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.ProjectRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.ProjectResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.User;
import com.pedromolon.demoprojectanddemandmanagementsystem.service.ProjectService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @Test
    @WithMockUser
    void quandoCriarProjeto_deveRetornarSucesso() throws Exception {
        ProjectRequest request = new ProjectRequest(
                "Novo projeto",
                "Descrição do projeto",
                LocalDate.of(2025, 1, 1),
                null
        );

        ProjectResponse response = new ProjectResponse(
                1L,
                "Novo projeto",
                "Descrição do projeto",
                LocalDate.of(2025, 1, 1),
                null,
                1L,
                "Pedro"
        );

        when(projectService.createProject(any(ProjectRequest.class), nullable(User.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Novo projeto"))
                .andExpect(jsonPath("$.description").value("Descrição do projeto"));
    }

    @Test
    @WithMockUser
    void quandoBuscarTodosProjetos_deveRetornarPaginaDeProjetos() throws Exception {
        ProjectResponse response = new ProjectResponse(
                1L, "Projeto Teste", "Descrição",
                LocalDate.now(), null, 1L, "Pedro"
        );

        Page<ProjectResponse> projectResponses = new PageImpl<>(
                List.of(response),
                PageRequest.of(0, 10),
                1
        );

        when(projectService.findAllProjects(any(Pageable.class)))
                .thenReturn(projectResponses);

        mockMvc.perform(
                get("/projects")
                        .param("page", "0")
                        .param("size", "10")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Projeto Teste"));
    }

}
