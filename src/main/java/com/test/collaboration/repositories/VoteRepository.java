package com.test.collaboration.repositories;

import com.test.collaboration.entities.Employee;
import com.test.collaboration.entities.Idea;
import com.test.collaboration.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByIdeaAndVoter(Idea idea, Employee voter);
    long countByIdea(Idea idea);
}
