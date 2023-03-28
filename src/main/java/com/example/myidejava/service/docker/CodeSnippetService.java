package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.CodeSnippetSearch;
import com.example.myidejava.dto.docker.CodeSnippetSearchResponse;
import com.example.myidejava.mapper.CodeSnippetMapper;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import com.example.myidejava.repository.docker.CodeSnippetSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CodeSnippetService {
    private final ContainerService containerService;
    private final CodeSnippetRepository codeSnippetRepository;
    private final CodeSnippetMapper codeSnippetMapper;


    @Transactional(readOnly = true)
    public CodeSnippet getCodeSnippetById(Long codeSnippetId) {
        return codeSnippetRepository.findById(codeSnippetId).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CODE_SNIPPET);
        });
    }

    @Transactional(readOnly = true)
    public CodeSnippetResponse getCodeSnippetResponse(Long codeSnippetId) {
        CodeSnippet codeSnippet = getCodeSnippetById(codeSnippetId);
        return codeSnippetMapper.INSTANCE.toCodeSnippetResponse(codeSnippet);
    }

    @Transactional(readOnly = true)
    public CodeSnippetSearchResponse getCodeSnippetSearchResponse(CodeSnippetSearch codeSnippetSearch, Pageable pageable) {
        Specification<CodeSnippet> specification = ((root, query, criteriaBuilder) -> null);
        if (codeSnippetSearch != null) {
            if (codeSnippetSearch.getContainerId() != null) {
                Container container = containerService.getContainerById(codeSnippetSearch.getContainerId());
                specification = specification.and(CodeSnippetSpecification.equalContainer(container));
            }
            if (codeSnippetSearch.getRequest() != null)
                specification = specification.and(CodeSnippetSpecification.containRequest(codeSnippetSearch.getRequest()));
            if (codeSnippetSearch.getIsSuccess() != null)
                specification = specification.and(CodeSnippetSpecification.equalIsSuccess(codeSnippetSearch.getIsSuccess()));
        }

        Page<CodeSnippet> codeSnippetPage = codeSnippetRepository.findAll(specification, pageable);
        List<CodeSnippetResponse> codeSnippetResponses = codeSnippetMapper.INSTANCE.toCodeSnippetResponses(codeSnippetPage.getContent());

        return CodeSnippetSearchResponse.builder()
                .codeSnippetResponses(codeSnippetResponses)
                .totalCount(codeSnippetPage.getTotalElements())
                .totalPage(codeSnippetPage.getTotalPages())
                .currentCount(codeSnippetPage.getNumberOfElements())
                .hasPrev(codeSnippetPage.hasPrevious())
                .hasNext(codeSnippetPage.hasNext())
                .build();
    }


}
