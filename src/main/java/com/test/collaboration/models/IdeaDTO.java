package com.test.collaboration.models;

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
public class IdeaDTO {
    private Long id;
    private String title;
    private String description;
    private Set<String> tagNames;
    private EmployeeDTO createdBy;
    private LocalDateTime createdAt;
}
