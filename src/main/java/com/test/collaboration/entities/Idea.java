package com.test.collaboration.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime createdAt;

    @ManyToOne
    private Employee createdBy;

    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;

    @OneToMany(mappedBy = "idea")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vote> votes;
}
