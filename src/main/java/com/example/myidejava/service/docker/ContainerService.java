package com.example.myidejava.service.docker;

import com.example.myidejava.core.AppProperty;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.core.util.docker.MyDockerClient;
import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.mapper.CodeSnippetMapper;
import com.example.myidejava.mapper.ContainerMapper;
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
    private final AppProperty appProperty;
    private final MyDockerClient myDockerClient;
    private final ContainerRepository containerRepository;
    private final CodeSnippetRepository codeSnippetRepository;
    private final ContainerMapper containerMapper;
    private final CodeSnippetMapper codeSnippetMapper;
//    private final CodeExecutorFactory codeExecutorFactory;

    public void initialize() {
        List<ContainerResponse> containers = myDockerClient.getAllContainers();
        containers.forEach(containerResponse -> {
            // todo : 만약 컨테이너가 올라와 있지 않다면, docker-compose 실행 하도록 코드 추가.
            if (appProperty.isContainDockerImageName(containerResponse.getDockerImageName())) {
                createOrUpdate(containerResponse);
            }
        });
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
        return containerMapper.INSTANCE.ofDtoList(containerRepository.findAll());
    }

    public List<ContainerResponse> getAllContainersOnServer() {
        return myDockerClient.getAllContainers();
    }

    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CONTAINER);
        });
    }

    @Transactional(readOnly = true)
    public List<CodeSnippetResponse> getContainerCodeSnippets(Long containerId) {
        Container container = getContainerById(containerId);
        return codeSnippetMapper.INSTANCE.ofDtoList(container.getCodeSnippetList());
    }

    public CodeResponse executeCode(Long containerId, CodeRequest codeRequest) {
        Container container = getContainerById(containerId);
        // 코드 스니펫 생성
        CodeSnippet codeSnippet = CodeSnippet.create(container, codeRequest, Optional.empty());
        codeSnippetRepository.save(codeSnippet);
        // 코드 실행
        CodeResponse codeResponse = myDockerClient.executeCode(container, codeRequest);
//        CodeExecutor codeExecutor = codeExecutorFactory.create(container);
//        CodeResponse codeResponse = codeExecutor.execute(container, codeRequest.getCode());
        // 실행 결과 저장
        codeSnippet.saveResponse(codeResponse.toMap());
        return codeResponse;
    }

}
