package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.CodeSnippetSearch;
import com.example.myidejava.dto.docker.CodeSnippetSearchResponse;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CodeSnippetServiceTest {
    @Autowired
    CodeSnippetRepository codeSnippetRepository;
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    CodeSnippetService codeSnippetService;

    @BeforeEach
    public void beforeEach() {
        containerRepository.findAll().forEach(container -> {
            CodeSnippet codeSnippet = CodeSnippet.create(container, CodeRequest.builder().code(container.getDockerImageName()).build(), null);
            codeSnippetRepository.save(codeSnippet);
        });
    }

    @Test
    @DisplayName("getCodeSnippetById 성공")
    void test_getCodeSnippetById() {
        // given
        CodeSnippet findCodeSnippet = codeSnippetRepository.findAll().get(0);
        // when
        CodeSnippet codeSnippet = codeSnippetService.getCodeSnippetById(findCodeSnippet.getId());
        CodeSnippetResponse codeSnippetResponse = codeSnippetService.getCodeSnippetResponse(codeSnippet.getId());
        // then
        Assertions.assertEquals(codeSnippetResponse.getId(), codeSnippet.getId());
        Assertions.assertEquals(codeSnippetResponse.getRequest(), codeSnippet.getRequest());
        Assertions.assertEquals(codeSnippetResponse.getResponse(), codeSnippet.getResponse());
        Assertions.assertEquals(codeSnippetResponse.getIsSuccess(), codeSnippet.getIsSuccess());
    }

    @Test
    @DisplayName("getCodeSnippetById 실패")
    void test_getCodeSnippetById_raise() {
        // given
        Long codeSnippetId = 0L;
        // when then
        Assertions.assertThrows(NotFoundException.class, () -> codeSnippetService.getCodeSnippetById(codeSnippetId));
        Assertions.assertThrows(NotFoundException.class, () -> codeSnippetService.getCodeSnippetById(codeSnippetId));
    }

    @Test
    @DisplayName("CodeSnippetSearch 검색 - 1")
    void test_getCodeSnippetSearchResponse_1() {
        // given
//        CodeSnippetSearch codeSnippetSearch = new CodeSnippetSearch(1L, "print", 1);
        CodeSnippetSearch codeSnippetSearch = new CodeSnippetSearch();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt"));
        // when
        CodeSnippetSearchResponse codeSnippetSearchResponse = codeSnippetService.getCodeSnippetSearchResponse(codeSnippetSearch, pageRequest);
        // then
        Assertions.assertNotNull(codeSnippetSearchResponse.getCodeSnippetResponses());
        Assertions.assertNotNull(codeSnippetSearchResponse.getTotalCount());
        Assertions.assertNotNull(codeSnippetSearchResponse.getTotalPage());
        Assertions.assertNotNull(codeSnippetSearchResponse.getCurrentCount());
        Assertions.assertNotNull(codeSnippetSearchResponse.getHasPrev());
        Assertions.assertNotNull(codeSnippetSearchResponse.getHasNext());
    }

    @Test
    @DisplayName("CodeSnippetSearch 검색 - 2")
    void test_getCodeSnippetSearchResponse_2() {
        // given
        CodeSnippetSearch codeSnippetSearch = CodeSnippetSearch.builder().containerId(1L).build();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt"));
        // when
        CodeSnippetSearchResponse codeSnippetSearchResponse = codeSnippetService.getCodeSnippetSearchResponse(codeSnippetSearch, pageRequest);
        // then
        Assertions.assertNotNull(codeSnippetSearchResponse.getCodeSnippetResponses());
    }

    @Test
    @DisplayName("CodeSnippetSearch 검색 - 3")
    void test_getCodeSnippetSearchResponse_3() {
        // given
        CodeSnippetSearch codeSnippetSearch = CodeSnippetSearch.builder().containerId(1L).request("print").isSuccess(1).build();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt"));
        // when
        CodeSnippetSearchResponse codeSnippetSearchResponse = codeSnippetService.getCodeSnippetSearchResponse(codeSnippetSearch, pageRequest);
        // then
        Assertions.assertNotNull(codeSnippetSearchResponse.getCodeSnippetResponses());
    }

}