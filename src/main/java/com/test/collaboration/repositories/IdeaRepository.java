package com.test.collaboration.repositories;

import com.test.collaboration.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    @Query("SELECT DISTINCT i FROM Idea i LEFT JOIN FETCH i.votes")
    List<Idea> findAllWithVotes();
}
