package com.example.myidejava.mapper;

import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CodeSnippetMapper {

    CodeSnippetMapper INSTANCE = Mappers.getMapper(CodeSnippetMapper.class);

    List<CodeSnippetResponse> toCodeSnippetResponse(List<CodeSnippet> codeSnippets);

}
