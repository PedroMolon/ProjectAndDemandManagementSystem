package com.pedromolon.demoprojectanddemandmanagementsystem.repository;

import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
