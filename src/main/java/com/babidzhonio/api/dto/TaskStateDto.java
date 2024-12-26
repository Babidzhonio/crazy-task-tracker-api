package com.babidzhonio.api.dto;

import com.babidzhonio.store.entities.TaskStateEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskStateDto {

    private Long id;
    private String name;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("left_task_state_id")
    Long leftTaskStateId;
    @JsonProperty("right_task_state_id")
    Long rightTaskStateId;
    List<TaskDto> tasks;
}
