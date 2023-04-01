package com.example.myidejava.controller.docker;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.repository.docker.ContainerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ContainerControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ContainerRepository containerRepository;

    @Test
    @DisplayName("GET /api/v1/containers")
    void containers() throws Exception {
        mvc.perform(get("/api/v1/containers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value("1")) ;
    }

    @Test
    @DisplayName("GET /api/v1/containers/on-server Fail")
    void containersOnServer_fail() throws Exception {
        mvc.perform(get("/api/v1/containers/on-server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/v1/containers/on-server")
    @WithMockUser(username = "test_user", password = "passw0rd", roles = {"USER", "ADMIN"})
    void containersOnServer() throws Exception {
        mvc.perform(get("/api/v1/containers/on-server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/containers/{container_id}")
    void container() throws Exception {
        List<Container> containers = containerRepository.findAll();
        containers.forEach(container -> {
            try {
                mvc.perform(get("/api/v1/containers/" + container.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("GET /api/v1/containers/{container_id}/code")
    void executeCodeOnContainer() throws Exception {
        Container container = containerRepository.findByLanguageNameAndLanguageVersion("Python", "2.7").orElseThrow();
        CodeRequest codeRequest = CodeRequest.builder().code("print(123)").build();

        mvc.perform(post("/api/v1/containers/" + container.getId() + "/code")
                        .content(objectMapper.writeValueAsString(codeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}