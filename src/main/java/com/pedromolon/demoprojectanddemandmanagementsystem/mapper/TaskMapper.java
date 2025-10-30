package com.pedromolon.demoprojectanddemandmanagementsystem.mapper;

import com.pedromolon.demoprojectanddemandmanagementsystem.dto.response.TaskResponse;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Task;
import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toResponse(Task task);

    Task toEntity(TaskResponse taskResponse);

}
