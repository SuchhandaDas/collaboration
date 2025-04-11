package com.test.collaboration.repositories;

import com.test.collaboration.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
}
