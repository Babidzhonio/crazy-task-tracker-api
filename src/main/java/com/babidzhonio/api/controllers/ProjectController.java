package com.babidzhonio.api.controllers;

import com.babidzhonio.api.dto.ProjectDto;
import com.babidzhonio.api.exceptions.BadRequestException;
import com.babidzhonio.api.exceptions.NotFoundException;
import com.babidzhonio.api.factories.ProjectDtoFactory;
import com.babidzhonio.store.entities.ProjectEntity;
import com.babidzhonio.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name){

        if(name.trim().isEmpty()){
            throw new BadRequestException("Name can't be empty");
        }

        projectRepository
                .findByName(name)
                .ifPresent(project ->{
                    throw new BadRequestException(
                            String.format("Project \"%s\" already exists.", name)
                    );
                });
        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );
        return projectDtoFactory.makeProjectDto(project);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@RequestParam String name, @PathVariable("project_id") Long projectId){

        if(name.trim().isEmpty()){
            throw new BadRequestException("Name can't be empty");
        }

        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() ->
                new NotFoundException(String.format("Project with \"%s\" doesn't exists.", projectId)));


        projectRepository
                .findByName(name)
                .filter(anotherProject -> !Objects.equals(projectId, anotherProject.getId()))
                .ifPresent(anotherProject ->{
                    throw new BadRequestException(
                            String.format("Project \"%s\" already exists.", name)
                    );
                });

        project.setName(name);
        project = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }
}
