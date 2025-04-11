package com.test.collaboration.builders;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Idea;
import com.test.collaboration.entities.Tag;
import com.test.collaboration.models.EmployeeDTO;
import com.test.collaboration.models.IdeaDTO;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class IdeaBuilder {
    public static Idea buildIdeaEntity(IdeaDTO ideaDTO, Set<Tag> tags, Employee creator) {
        return Idea.builder()
                .title(ideaDTO.getTitle())
                .description(ideaDTO.getDescription())
                .tags(tags)
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .build();

    }

    public static IdeaDTO buildIdeaDTO(Idea idea) {
        return IdeaDTO.builder()
                .id(idea.getId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .tagNames(idea.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .createdBy(EmployeeDTO.builder().id(idea.getCreatedBy().getId()).name(idea.getCreatedBy().getName()).build())
                .createdAt(LocalDateTime.now())
                .voteCount(CollectionUtils.isEmpty(idea.getVotes()) ? 0 : idea.getVotes().size())
                .build();
    }
}
