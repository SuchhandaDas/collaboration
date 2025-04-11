package com.test.collaboration.services.impl;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Idea;
import com.test.collaboration.entities.Tag;
import com.test.collaboration.exceptions.EmployeeNotFoundException;
import com.test.collaboration.exceptions.InvalidIdeaRequestException;
import com.test.collaboration.exceptions.InvalidTagsException;
import com.test.collaboration.models.IdeaDTO;
import com.test.collaboration.repositories.EmployeeRepository;
import com.test.collaboration.repositories.TagRepository;
import com.test.collaboration.services.IdeaService;

import java.util.HashSet;
import java.util.Set;

import static com.test.collaboration.builders.IdeaBuilder.buildIdeaDTO;
import static com.test.collaboration.builders.IdeaBuilder.buildIdeaEntity;
import static com.test.collaboration.validators.IdeaValidator.isValid;

public class IdeaServiceImpl implements IdeaService {
    private final EmployeeRepository employeeRepo;
    private final TagRepository tagRepo;

    public IdeaServiceImpl(EmployeeRepository employeeRepo, TagRepository tagRepo) {
        this.employeeRepo = employeeRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public IdeaDTO createIdea(Long employeeId, IdeaDTO ideaDTO) {
        // VALIDATE ideaDTO
        if(!isValid(ideaDTO)) throw new InvalidIdeaRequestException("Invalid idea request");

        // Validate creator
        Employee creator = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("EmployeeID: " + employeeId + " not found"));

        // Get tags
        Set<Tag> tags = new HashSet<>();
        for (String name : ideaDTO.getTagNames()) {
            tagRepo.findByName(name).ifPresent(tags::add);
        }

        // validate tags present
        if(tags.isEmpty())
            throw new InvalidTagsException("No valid tags found in tagListSize: " + ideaDTO.getTagNames().size());

        // create idea
        Idea idea = buildIdeaEntity(ideaDTO, tags, creator);

        return buildIdeaDTO(idea);
    }
}
