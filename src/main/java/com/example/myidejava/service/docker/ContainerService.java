package com.example.myidejava.service.docker;

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
import com.example.myidejava.module.kafka.MyKafkaProducer;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import com.example.myidejava.repository.docker.ContainerRepository;
import com.example.myidejava.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService {
    private final ContainerRepository containerRepository;
    private final CodeSnippetRepository codeSnippetRepository;
    private final MemberService memberService;
    private final ContainerMapper containerMapper;
    private final CodeSnippetMapper codeSnippetMapper;
    private final DockerClientShortCut dockerClientShortCut;
    private final CodeExecutorFactory codeExecutorFactory;
    private final ContainerValidationService containerValidationService;
    private final MyKafkaProducer kafkaProducer;

    public void initialize() {
        List<ContainerResponse> containers = dockerClientShortCut.getContainers();
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
    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CONTAINER);
        });
    }

    @Transactional(readOnly = true)
    public ContainerResponse getContainer(Long containerId) {
        return containerMapper.INSTANCE.toContainerResponse(getContainerById(containerId));
    }

    @Transactional(readOnly = true)
    public List<ContainerResponse> getContainers() {
        return containerMapper.INSTANCE.toContainerResponses(containerRepository.findAll());
    }

    public List<ContainerResponse> getContainersOnServer() {
        return dockerClientShortCut.getContainers();
    }

    public CodeSnippetResponse executeCode(Long containerId, CodeRequest codeRequest, Authentication authentication) {
        Container container = getContainerById(containerId);
        containerValidationService.validateIsContainerRunning(container);
        // 코드 스니펫 생성
        CodeSnippet codeSnippet;
        if (authentication != null) {
            codeSnippet = CodeSnippet.create(container, codeRequest, memberService.getMemberByEmail(authentication.getName()));
        } else {
            codeSnippet = CodeSnippet.create(container, codeRequest, null);
        }
        codeSnippetRepository.save(codeSnippet);
        // 코드 실행
        ContainerCodeExecutor codeExecutor = codeExecutorFactory.create(container);
        CodeResponse codeResponse = codeExecutor.execute(container, codeRequest);
        // 실행 결과 저장
        codeSnippet.saveResponse(codeResponse.toMap());
        return codeSnippetMapper.INSTANCE.toCodeSnippetResponse(codeSnippet);
    }

    public CodeSnippetResponse requestCodeSnippetToExecute(Long containerId, CodeRequest codeRequest, Authentication authentication) {
        // todo refactoring
        Container container = getContainerById(containerId);
        containerValidationService.validateIsContainerRunning(container);
        // 코드 스니펫 생성
        CodeSnippet codeSnippet;
        if (authentication != null) {
            codeSnippet = CodeSnippet.create(container, codeRequest, memberService.getMemberByEmail(authentication.getName()));
        } else {
            codeSnippet = CodeSnippet.create(container, codeRequest, null);
        }
        codeSnippetRepository.save(codeSnippet);
        kafkaProducer.send("CODE_SNIPPET", codeSnippet.getId().toString(), "execute");
        return codeSnippetMapper.INSTANCE.toCodeSnippetResponse(codeSnippet);
    }

    public void execute(Long codeSnippetId) {
        // todo refactoring
        CodeSnippet codeSnippet = codeSnippetRepository.findById(codeSnippetId).orElseThrow(() -> {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CODE_SNIPPET);
        });
        Container container = codeSnippet.getContainer();
        ContainerCodeExecutor codeExecutor = codeExecutorFactory.create(container);
        CodeResponse codeResponse = codeExecutor.execute(container, CodeRequest.builder().code(codeSnippet.getRequest()).build());
        // 실행 결과 저장
        codeSnippet.saveResponse(codeResponse.toMap());
    }

}
