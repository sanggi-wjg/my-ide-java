package com.example.myidejava.repository.docker;

import com.example.myidejava.domain.docker.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {
    Optional<Container> findByLanguageNameAndLanguageVersion(
            String languageName, String languageVersion
    );
}
