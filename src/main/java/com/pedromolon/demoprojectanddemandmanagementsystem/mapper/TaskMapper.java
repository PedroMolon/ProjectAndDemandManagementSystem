package com.pedromolon.demoprojectanddemandmanagementsystem.mapper;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.request.TaskRequest;
import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    TaskResponse toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "user", ignore = true)
    Task toEntity(TaskRequest request);

}
