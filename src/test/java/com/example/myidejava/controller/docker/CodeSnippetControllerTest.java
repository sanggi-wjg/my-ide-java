package com.example.myidejava.controller.docker;

import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import com.example.myidejava.repository.docker.ContainerRepository;
import com.example.myidejava.service.docker.CodeSnippetService;
import com.example.myidejava.service.docker.ContainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CodeSnippetControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    CodeSnippetRepository codeSnippetRepository;
    @Autowired
    ContainerRepository containerRepository;

    public void createCodeSnippets() {
        Random random = new Random();
        List<Container> containers = containerRepository.findAll();

        IntStream.range(0, 30).forEach(i -> {
            CodeSnippet codeSnippet = CodeSnippet.builder()
                    .container(containers.get(random.nextInt(0, containers.size() - 1)))
                    .request("print(123)")
                    .build();
            codeSnippetRepository.save(codeSnippet);
        });
    }

    @BeforeEach
    public void beforeEach() {
        createCodeSnippets();
    }

    @Test
    @DisplayName("GET /api/v1/code-snippets")
    void codeSnippets() throws Exception {
        // todo : search detail
        mvc.perform(get("/api/v1/code-snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/code-snippets/{code_snippet_id}")
    void codeSnippetById() throws Exception {
        mvc.perform(get("/api/v1/code-snippets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}