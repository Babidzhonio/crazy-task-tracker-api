package com.babidzhonio.store.repositories;

import com.babidzhonio.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}