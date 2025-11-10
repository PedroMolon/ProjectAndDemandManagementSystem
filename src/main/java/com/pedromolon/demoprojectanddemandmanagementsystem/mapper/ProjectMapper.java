package com.pedromolon.demoprojectanddemandmanagementsystem.mapper;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.ProjectRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.ProjectResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    ProjectResponse toResponse(Project project);

    Project toEntity(ProjectRequest projectRequest);

}
