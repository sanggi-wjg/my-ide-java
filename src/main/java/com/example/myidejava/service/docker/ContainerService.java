package com.example.myidejava.service.docker;

import com.example.myidejava.core.AppProperty;
import com.example.myidejava.core.util.MyDockerClient;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.ContainerDto;
import com.example.myidejava.mapper.ContainerMapper;
import com.example.myidejava.repository.docker.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService {
    private final AppProperty appProperty;
    private final MyDockerClient myDockerClient;
    private final ContainerRepository containerRepository;
    private final ContainerMapper containerMapper;

    public void initialize() {
        List<ContainerDto> containers = myDockerClient.getAllContainers();
        containers.forEach(containerDto -> {
            // todo : 만약 컨테이너가 올라와 있지 않다면, docker-compose 실행 하도록 코드 추가.
            if (appProperty.isContainDockerImageName(containerDto.getDockerImageName())) {
                createOrUpdate(containerDto);
            }
        });
    }

    public void createOrUpdate(ContainerDto containerDto) {
        containerRepository.findByLanguageNameAndLanguageVersion(containerDto.getLanguageName(), containerDto.getLanguageVersion())
                .ifPresentOrElse(
                        container -> {
                            container.saveContainerInfo(containerDto);
                        },
                        () -> {
                            Container container = containerMapper.INSTANCE.toEntity(containerDto);
                            container.saveCodeExecutorType();
                            containerRepository.save(container);
                        }
                );
    }

    @Transactional(readOnly = false)
    public List<ContainerDto> getAllContainers() {
        Container container = Container.builder().containerId("123").dockerImageName("docker-php-7.6").languageName("php").languageVersion("7.6").containerStatus("rr").containerState("aa").build();
        containerRepository.save(container);
        return containerMapper.INSTANCE.ofDtoList(containerRepository.findAll());
    }
}
