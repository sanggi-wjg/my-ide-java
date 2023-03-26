package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CodeSnippetServiceTest {

    @Autowired
    CodeSnippetService codeSnippetService;

    @Test
    @DisplayName("initialize 성공")
    void test_getCodeSnippetById_raise() {
        // given
        Long codeSnippetId = 0L;
        // when then
        Assertions.assertThrows(NotFoundException.class, () -> codeSnippetService.getCodeSnippetById(codeSnippetId));
        Assertions.assertThrows(NotFoundException.class, () -> codeSnippetService.getCodeSnippetById(codeSnippetId));
    }

}