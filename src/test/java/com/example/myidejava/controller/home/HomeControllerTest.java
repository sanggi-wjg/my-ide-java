package com.example.myidejava.controller.home;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /")
    void test_getHome() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /health")
    void test_getHealth() throws Exception {
        mvc.perform(get("/health"))
                .andExpect(status().isOk());
    }

}