package com.test.collaboration.services;

import com.test.collaboration.entities.*;
import com.test.collaboration.exceptions.*;
import com.test.collaboration.models.IdeaDTO;
import com.test.collaboration.repositories.*;
import com.test.collaboration.services.impl.IdeaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdeaServiceImplTest {

    @Mock private EmployeeRepository employeeRepo;
    @Mock private TagRepository tagRepo;
    @Mock private IdeaRepository ideaRepo;
    @Mock private VoteRepository voteRepo;
    @Mock private CollaborationRepository collaborationRepo;

    @InjectMocks
    private IdeaServiceImpl ideaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIdea_success() {
        Long employeeId = 1L;
        String tagName = "Innovation";

        IdeaDTO dto = IdeaDTO.builder().title("Test Title").description("Test Description").tagNames(Set.of(tagName)).build();

        Employee employee = Employee.builder().id(employeeId).name("Alice").build();
        Tag tag = Tag.builder().id(1L).name(tagName).build();

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(employee));
        when(tagRepo.findByName(tagName)).thenReturn(Optional.of(tag));
        when(ideaRepo.save(any(Idea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IdeaDTO result = ideaService.createIdea(employeeId, dto);
        assertEquals(dto.getTitle(), result.getTitle());
        verify(ideaRepo).save(any(Idea.class));
    }

    @Test
    void testCreateIdea_invalidTags_throwsException() {
        IdeaDTO dto = IdeaDTO.builder().title("Test Title").description("Test Description").tagNames(Set.of("Invalid")).build();
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(Employee.builder().id(1L).build()));
        when(tagRepo.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidTagsException.class, () -> ideaService.createIdea(1L, dto));
    }


    @Test
    void testVoteIdea_success() {
        Long ideaId = 1L;
        Long voterId = 2L;
        Employee creator = Employee.builder().id(3L).name("A").build();
        Employee voter = Employee.builder().id(voterId).name("B").build();
        Idea idea = Idea.builder().id(ideaId).createdBy(creator).build();

        when(ideaRepo.findById(ideaId)).thenReturn(Optional.of(idea));
        when(employeeRepo.findById(voterId)).thenReturn(Optional.of(voter));
        when(voteRepo.existsByIdeaAndVoter(idea, voter)).thenReturn(false);

        assertDoesNotThrow(() -> ideaService.voteIdea(ideaId, voterId));
        verify(voteRepo).save(any(Vote.class));
    }

    @Test
    void testVoteIdea_selfVote_throwsException() {
        Long id = 1L;
        Employee employee = Employee.builder().id(id).build();
        Idea idea = Idea.builder().id(1L).createdBy(employee).build();

        when(ideaRepo.findById(id)).thenReturn(Optional.of(idea));

        assertThrows(CreatorSelfUpvoteException.class, () -> ideaService.voteIdea(id, id));
    }

    @Test
    void testCollaborate_success() {
        Long ideaId = 1L;
        Long collaboratorId = 2L;
        Employee creator = Employee.builder().id(3L).name("A").build();
        Employee employee = Employee.builder().id(collaboratorId).name("B").build();
        Idea idea = Idea.builder().id(ideaId).createdBy(creator).build();

        when(ideaRepo.findById(ideaId)).thenReturn(Optional.of(idea));
        when(employeeRepo.findById(collaboratorId)).thenReturn(Optional.of(employee));
        when(collaborationRepo.existsByIdeaAndCollaborator(idea, employee)).thenReturn(false);

        assertDoesNotThrow(() -> ideaService.collaborate(ideaId, collaboratorId));
        verify(collaborationRepo).save(any(Collaboration.class));
    }

    @Test
    void testGetCollaborators_returnsList() {
        Employee emp = Employee.builder().id(2L).name("Tester").build();
        Collaboration collab = Collaboration.builder().collaborator(emp).build();
        Idea idea = Idea.builder().id(1L).build();

        when(ideaRepo.findById(1L)).thenReturn(Optional.of(idea));
        when(collaborationRepo.findByIdea(idea)).thenReturn(List.of(collab));

        List<String> result = ideaService.getCollaborators(1L);
        assertEquals(List.of("Tester"), result);
    }
}
