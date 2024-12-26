package com.babidzhonio.api.controllers;

import com.babidzhonio.api.controllers.helpers.ControllerHelpers;
import com.babidzhonio.api.dto.AckDto;
import com.babidzhonio.api.dto.ProjectDto;
import com.babidzhonio.api.exceptions.BadRequestException;
import com.babidzhonio.api.exceptions.NotFoundException;
import com.babidzhonio.api.factories.ProjectDtoFactory;
import com.babidzhonio.store.entities.ProjectEntity;
import com.babidzhonio.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    private final ControllerHelpers controllerHelpers;
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String FETCH_PROJECTS = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

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


        controllerHelpers.getProjectOrThrowException(projectId);

        project.setName(name);
        project = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId){

        controllerHelpers.getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);
    }
}
