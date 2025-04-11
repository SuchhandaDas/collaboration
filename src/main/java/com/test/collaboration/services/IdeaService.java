package com.test.collaboration.services;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.models.IdeaDTO;

import java.util.List;

public interface IdeaService {
    IdeaDTO createIdea(Long employeeId, IdeaDTO ideaDTO);

    List<IdeaDTO> getAllIdeas(String sortBy);

    void voteIdea(Long ideaId, Long voterId);

    void collaborate(Long ideaId, Long collaboratorId);

    List<String> getCollaborators(Long ideaId);
}
