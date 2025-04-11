package com.test.collaboration.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdeaDTO {
    private Long id;
    private String title;
    private String description;
    private Set<String> tagNames;
    private EmployeeDTO createdBy;
    private LocalDateTime createdAt;
    private Integer voteCount;
}
