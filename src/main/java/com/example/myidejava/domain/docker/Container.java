package com.example.myidejava.domain.docker;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "container",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_container_language_version", columnNames = {"language_name", "language_version"})
        }
)
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @Column(name = "language", nullable = false)
    private Language language;
}
