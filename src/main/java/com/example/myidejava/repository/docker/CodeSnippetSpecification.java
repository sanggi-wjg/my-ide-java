package com.example.myidejava.repository.docker;

import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.domain.docker.Container;
import org.springframework.data.jpa.domain.Specification;

public class CodeSnippetSpecification {

    public static Specification<CodeSnippet> equalContainer(Container container) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("container"), container));
    }

    public static Specification<CodeSnippet> equalIsSuccess(Integer isSuccess) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isSuccess"), isSuccess == 1));
    }

    public static Specification<CodeSnippet> containRequest(String request) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("request"), "%" + request + "%"));
    }

    private CodeSnippetSpecification() {
    }
}
