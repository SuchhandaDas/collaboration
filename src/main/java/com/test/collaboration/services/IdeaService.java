package com.test.collaboration.services;

import com.test.collaboration.models.IdeaDTO;

public interface IdeaService {
    IdeaDTO createIdea(Long employeeId, IdeaDTO ideaDTO);
}
