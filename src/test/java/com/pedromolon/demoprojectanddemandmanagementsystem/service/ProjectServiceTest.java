package com.pedromolon.demoprojectanddemandmanagementsystem.service;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.ProjectRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.ProjectResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.User;
import com.pedromolon.demoprojectanddemandmanagementsystem.mapper.ProjectMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    LocalDate startDate = LocalDate.of(2025, 10, 1);
    LocalDate endDate = LocalDate.of(2025, 10, 2);

    @Test
    void quandoCriarProjeto_deveRetornarSucesso() {
        ProjectRequest request = new ProjectRequest("Novo projeto", "Descrição", startDate, endDate);
        User user = new User();
        user.setId(1L);
        user.setName("Pedro");

        Project saveProject = new Project();
        ProjectResponse response = new ProjectResponse(1L, "Novo Projeto", "Descrição", startDate, endDate, 1L, "Pedro");

        when(projectMapper.toEntity(any(ProjectRequest.class))).thenReturn(saveProject);
        when(projectRepository.save(any(Project.class))).thenReturn(saveProject);
        when(projectMapper.toResponse(saveProject)).thenReturn(response);

        ProjectResponse projectResponse = projectService.createProject(request, user);

        assertNotNull(projectResponse);
        assertEquals(response.name(), projectResponse.name());
        assertEquals(response.description(), projectResponse.description());
        assertEquals(response.startDate(), projectResponse.startDate());
        assertEquals(response.endDate(), projectResponse.endDate());
        assertEquals(response.userId(), projectResponse.userId());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void quandoBuscarTodosProjetos_deveRetornarPaginaDeProjetos() {
        User user = new User();
        user.setId(1L);
        user.setName("Pedro");

        Pageable pageable = PageRequest.of(0, 10);

        Project project = new Project();
        project.setId(1L);
        project.setName("Novo Projeto");
        project.setDescription("Descrição");
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setUser(user);

        List<Project> projects = List.of(project);
        Page<Project> projectPage = new PageImpl<>(projects, pageable, projects.size());

        ProjectResponse response = new ProjectResponse(
                1L, "Novo Projeto", "Descrição", startDate, endDate, 1L, "Pedro"
        );

        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(response);

        Page<ProjectResponse> projectResponses = projectService.findAllProjects(pageable);

        assertNotNull(projectResponses);
        assertEquals(1, projectPage.getTotalElements());
        assertEquals(response.name(), projectResponses.getContent().get(0).name());
        assertEquals(response.description(), projectResponses.getContent().get(0).description());
        assertEquals(response.startDate(), projectResponses.getContent().get(0).startDate());
        assertEquals(response.endDate(), projectResponses.getContent().get(0).endDate());
        assertEquals(response.userId(), projectResponses.getContent().get(0).userId());

        verify(projectRepository, times(1)).findAll(pageable);
    }

}
