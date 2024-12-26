package com.babidzhonio.api.controllers.helpers;


import com.babidzhonio.api.exceptions.NotFoundException;
import com.babidzhonio.store.entities.ProjectEntity;
import com.babidzhonio.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Transactional
@Component
public class ControllerHelpers {
    ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        projectId
                                )
                        )
                );
    }
}
