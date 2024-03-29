package com.example.myidejava.core.spy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class P6SpyFormatterTest {
    @Autowired
    P6SpyFormatter p6SpyFormatter;

    @Test
    @DisplayName("formatMessage")
    void test_formatMessage() {
        String message = p6SpyFormatter.formatMessage(
                1, "", 1L, "statement", "", "SELECT * FROM something", ""
        );
        assertNotNull(message);
    }

    @Test
    @DisplayName("formatMessage-2")
    void test_formatMessage_2() {
        String message = p6SpyFormatter.formatMessage(
                2, "", 1L, "statement", "", "", ""
        );
        assertEquals("", message);
    }

    @Test
    @DisplayName("formatMessage-3")
    void test_formatMessage_3() {
        String message = p6SpyFormatter.formatMessage(
                3, "", 1L, "statement", "", "create table", ""
        );
        assertNotNull(message);
    }

}