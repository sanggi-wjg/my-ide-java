package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.mapper.CodeSnippetMapper;
import com.example.myidejava.mapper.ContainerMapper;
import com.example.myidejava.module.docker.DockerClientShortCut;
import com.example.myidejava.module.docker.executor.CodeExecutorFactory;
import com.example.myidejava.module.docker.executor.ContainerCodeExecutor;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import com.example.myidejava.repository.docker.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService {
    private final ContainerRepository containerRepository;
    private final CodeSnippetRepository codeSnippetRepository;
    private final ContainerMapper containerMapper;
    private final CodeSnippetMapper codeSnippetMapper;
    private final DockerClientShortCut dockerClientShortCut;
    private final CodeExecutorFactory codeExecutorFactory;
    private final ContainerValidationService containerValidationService;

    public void initialize() {
        List<ContainerResponse> containers = dockerClientShortCut.getAllContainers();
        // todo : 만약 컨테이너가 올라와 있지 않다면, docker-compose 실행 하도록 코드 추가.
        containers.forEach(this::createOrUpdate);
    }

    public void createOrUpdate(ContainerResponse containerResponse) {
        containerRepository.findByLanguageNameAndLanguageVersion(containerResponse.getLanguageName(), containerResponse.getLanguageVersion())
                .ifPresentOrElse(
                        container -> container.saveContainerInfo(containerResponse),
                        () -> {
                            Container container = containerMapper.INSTANCE.toEntity(containerResponse);
                            container.saveCodeExecutorType();
                            containerRepository.save(container);
                        }
                );
    }

    @Transactional(readOnly = true)
    public List<ContainerResponse> getAllContainers() {
        return containerMapper.INSTANCE.toContainerResponse(containerRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CONTAINER);
        });
    }

    @Transactional(readOnly = true)
    public List<CodeSnippetResponse> getContainerCodeSnippets(Long containerId) {
        Container container = getContainerById(containerId);
        return codeSnippetMapper.INSTANCE.toCodeSnippetResponse(container.getCodeSnippetList());
    }


    public List<ContainerResponse> getAllContainersOnServer() {
        return dockerClientShortCut.getAllContainers();
    }

    public CodeResponse executeCode(Long containerId, CodeRequest codeRequest) {
        Container container = getContainerById(containerId);
        containerValidationService.validateIsContainerRunning(container);
        // 코드 스니펫 생성
        CodeSnippet codeSnippet = CodeSnippet.create(container, codeRequest, Optional.empty());
        codeSnippetRepository.save(codeSnippet);
        // 코드 실행
        ContainerCodeExecutor codeExecutor = codeExecutorFactory.create(container);
        CodeResponse codeResponse = codeExecutor.execute(container, codeRequest);
        // 실행 결과 저장
        codeSnippet.saveResponse(codeResponse.toMap());
        return codeResponse;
    }

}
