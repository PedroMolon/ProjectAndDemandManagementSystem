package com.pedromolon.demoprojectanddemandmanagementsystem.service;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.ProjectRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.ProjectResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import com.pedromolon.demoprojectanddemandmanagementsystem.mapper.ProjectMapper;
import com.pedromolon.demoprojectanddemandmanagementsystem.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        return projectMapper.toResponse(
                projectRepository.save(projectMapper.toEntity(request))
        );
    }

    public Page<ProjectResponse> findAllProjects(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAll(pageable);

        return projectPage.map(projectMapper::toResponse);
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found for id: " + id));
    }

}
