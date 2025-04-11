package com.test.collaboration.services.impl;

import com.test.collaboration.builders.IdeaBuilder;
import com.test.collaboration.entities.*;
import com.test.collaboration.enums.SortBy;
import com.test.collaboration.exceptions.*;
import com.test.collaboration.models.IdeaDTO;
import com.test.collaboration.repositories.*;
import com.test.collaboration.services.IdeaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.test.collaboration.builders.IdeaBuilder.buildIdeaDTO;
import static com.test.collaboration.builders.IdeaBuilder.buildIdeaEntity;
import static com.test.collaboration.validators.IdeaValidator.isValid;

@Service
public class IdeaServiceImpl implements IdeaService {
    private final EmployeeRepository employeeRepo;
    private final TagRepository tagRepo;
    private final IdeaRepository ideaRepo;
    private final VoteRepository voteRepo;
    private final CollaborationRepository collaborationRepository;

    public IdeaServiceImpl(EmployeeRepository employeeRepo, TagRepository tagRepo, IdeaRepository ideaRepo, VoteRepository voteRepo, CollaborationRepository collaborationRepository) {
        this.employeeRepo = employeeRepo;
        this.tagRepo = tagRepo;
        this.ideaRepo = ideaRepo;
        this.voteRepo = voteRepo;
        this.collaborationRepository = collaborationRepository;
    }

    @Transactional
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
        ideaRepo.save(idea);
        return buildIdeaDTO(idea);
    }

    @Override
    public List<IdeaDTO> getAllIdeas(String sortBy) {
        SortBy sortByEnum = null;

        //validate sortBy string
        try {
            sortByEnum = SortBy.valueOf(sortBy);
        } catch (IllegalArgumentException e) {
            throw new InvalidSortByException("Invalid sortBy: " + sortBy + " , can only be VOTES or CREATED_AT");
        }

        List<Idea> ideas = ideaRepo.findAll();

        switch (sortByEnum) {
            case VOTES:
                ideas.sort(Comparator.comparingLong(a -> a.getVotes().size()));
                break;
            case CREATED_AT:
                ideas.sort(Comparator.comparing(Idea::getCreatedAt).reversed());
        }

        return ideas.stream().map(IdeaBuilder::buildIdeaDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void voteIdea(Long ideaId, Long voterId) {
        Idea idea = ideaRepo.findById(ideaId)
                .orElseThrow(() -> new InvalidIdeaRequestException("IdeaID: " + ideaId + " not found"));

        if (idea.getCreatedBy().getId().equals(voterId)) {
            throw new CreatorSelfUpvoteException("You can't vote on your own idea");
        }

        Employee voter = employeeRepo.findById(voterId)
                .orElseThrow(() -> new EmployeeNotFoundException("EmployeeId: " + voterId + " not found"));

        if (voteRepo.existsByIdeaAndVoter(idea, voter)) {
            throw new InvalidVoteRequestException("Already voted");
        }

        Vote vote = Vote.builder()
                .idea(idea)
                .voter(voter)
                .build();

        voteRepo.save(vote);
    }

    @Transactional
    @Override
    public void collaborate(Long ideaId, Long collaboratorId) {
        Idea idea = ideaRepo.findById(ideaId)
                .orElseThrow(() -> new IdeaNotFoundException("IdeaId: " + ideaId + " not found"));

        Employee employee = employeeRepo.findById(collaboratorId)
                .orElseThrow(() -> new EmployeeNotFoundException("EmployeeId: " + collaboratorId + " not found"));

        if (collaborationRepository.existsByIdeaAndCollaborator(idea, employee)) {
            throw new DuplicateCollaborationRequest("Already collaborating");
        }

        Collaboration collaboration = Collaboration.builder()
                .collaborator(employee)
                .idea(idea)
                .build();

        collaborationRepository.save(collaboration);
    }

    @Override
    public List<String> getCollaborators(Long ideaId) {
        Idea idea = ideaRepo.findById(ideaId)
                .orElseThrow(() -> new IdeaNotFoundException("IdeaId: " + ideaId + " not found"));
        List<Collaboration> collaborations = collaborationRepository.findByIdea(idea);

        if (collaborations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Employee> result = new ArrayList<>();

        for (Collaboration c : collaborations) {
            result.add(c.getCollaborator());
        }
        return result.stream().map(Employee::getName).collect(Collectors.toList());
    }
}
