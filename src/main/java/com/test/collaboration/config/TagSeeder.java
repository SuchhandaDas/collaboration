package com.test.collaboration.config;

import com.test.collaboration.entities.Tag;
import com.test.collaboration.repositories.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagSeeder implements CommandLineRunner {
    private final TagRepository tagRepository;

    public TagSeeder(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> tags = List.of("Product", "Platform", "Identity", "Data Ingestion", "DevX", "Reports");
        for (int i = 0; i < tags.size(); i++) {
            if (!tagRepository.existsById((long) (i + 1))) {
                tagRepository.save(new Tag((long) (i + 1), tags.get(i)));
            }
        }
    }
}
