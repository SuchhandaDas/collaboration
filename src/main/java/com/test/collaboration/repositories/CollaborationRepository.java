package com.test.collaboration.repositories;

import com.test.collaboration.entities.Collaboration;
import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {
    boolean existsByIdeaAndCollaborator(Idea idea, Employee collaborator);
    Optional<Collaboration> findByIdea(Idea idea);
}
