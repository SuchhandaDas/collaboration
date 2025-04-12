package com.test.collaboration.controller;

import com.test.collaboration.controllers.IdeaController;
import com.test.collaboration.models.IdeaDTO;
import com.test.collaboration.services.IdeaService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdeaControllerTest {

    private IdeaService ideaService;
    private IdeaController ideaController;
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        ideaService = mock(IdeaService.class);
        ideaController = new IdeaController(ideaService);
        request = mock(HttpServletRequest.class);
    }

    @Test
    void createIdea_success() {
        IdeaDTO dto = new IdeaDTO();
        when(request.getAttribute("employeeId")).thenReturn("123");
        when(ideaService.createIdea(123L, dto)).thenReturn(dto);

        ResponseEntity<?> response = ideaController.createIdea(dto, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void createIdea_error() {
        IdeaDTO dto = new IdeaDTO();
        when(request.getAttribute("employeeId")).thenReturn("123");
        when(ideaService.createIdea(anyLong(), any())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = ideaController.createIdea(dto, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllIdeas_success() {
        IdeaDTO dto = new IdeaDTO();
        when(ideaService.getAllIdeas("votes")).thenReturn(List.of(dto));

        ResponseEntity<?> response = ideaController.getAllIdeas("votes");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(dto), response.getBody());
    }

    @Test
    void getAllIdeas_error() {
        when(ideaService.getAllIdeas("votes")).thenThrow(new RuntimeException("Failed"));

        ResponseEntity<?> response = ideaController.getAllIdeas("votes");

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed", response.getBody());
    }

    @Test
    void voteIdea_success() {
        when(request.getAttribute("employeeId")).thenReturn("456");

        ResponseEntity<?> response = ideaController.voteIdea(99L, request);

        verify(ideaService).voteIdea(99L, 456L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successfully voted", response.getBody());
    }

    @Test
    void voteIdea_error() {
        when(request.getAttribute("employeeId")).thenReturn("456");
        doThrow(new RuntimeException("Voting failed")).when(ideaService).voteIdea(anyLong(), anyLong());

        ResponseEntity<?> response = ideaController.voteIdea(99L, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Voting failed", response.getBody());
    }

    @Test
    void collaborate_success() {
        when(request.getAttribute("employeeId")).thenReturn("321");

        ResponseEntity<?> response = ideaController.collaborate(77L, request);

        verify(ideaService).collaborate(77L, 321L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successfully collaborating on Idea", response.getBody());
    }

    @Test
    void collaborate_error() {
        when(request.getAttribute("employeeId")).thenReturn("321");
        doThrow(new RuntimeException("Collaboration failed")).when(ideaService).collaborate(anyLong(), anyLong());

        ResponseEntity<?> response = ideaController.collaborate(77L, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Collaboration failed", response.getBody());
    }

    @Test
    void getCollaborators_success() {
        when(ideaService.getCollaborators(55L)).thenReturn(List.of("emp1", "emp2"));

        ResponseEntity<?> response = ideaController.getCollaborators(55L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of("emp1", "emp2"), response.getBody());
    }

    @Test
    void getCollaborators_error() {
        when(ideaService.getCollaborators(55L)).thenThrow(new RuntimeException("No data"));

        ResponseEntity<?> response = ideaController.getCollaborators(55L);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("No data", response.getBody());
    }
}
