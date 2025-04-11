package com.test.collaboration.controllers;

import com.test.collaboration.models.IdeaDTO;
import com.test.collaboration.services.IdeaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/ideas")
public class IdeaController {
    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createIdea(@RequestBody IdeaDTO ideaDTO, HttpServletRequest req) {
        try {
            String employeeId = (String) req.getAttribute("employeeId");
            return ResponseEntity.ok(ideaService.createIdea(Long.parseLong(employeeId), ideaDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllIdeas(@RequestParam String sortBy) {
        try {
            return ResponseEntity.ok(ideaService.getAllIdeas(sortBy));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/{id}/vote")
    public void voteIdea(@PathVariable Long id, HttpServletRequest req) {
        String employeeId = (String) req.getAttribute("employeeId");
        ideaService.voteIdea(id, Long.parseLong(employeeId));
    }

    @PostMapping("/{id}/collaborate")
    public void collaborate(@PathVariable Long id, HttpServletRequest req) {
        String employeeId = (String) req.getAttribute("employeeId");
        ideaService.collaborate(id, Long.parseLong(employeeId));
    }

    @GetMapping("/{id}/collaborators")
    public List<String> getCollaborators(@PathVariable Long id) {
        return ideaService.getCollaborators(id);
    }

}
