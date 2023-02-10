package com.example.myidejava.service.docker;

import com.example.myidejava.core.AppProperty;
import com.example.myidejava.core.util.MyDockerClient;
import com.example.myidejava.dto.docker.ContainerDto;
import com.example.myidejava.mapper.ContainerMapper;
import com.example.myidejava.repository.docker.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService {
    private final AppProperty appProperty;
    private final MyDockerClient myDockerClient;
    private final ContainerRepository containerRepository;
    private final ContainerMapper containerMapper;

    @PostConstruct
    public void init() {
        List<ContainerDto> containers = myDockerClient.getAllContainers();
        containers.forEach(containerDto -> {
            if (appProperty.isContainDockerImageName(containerDto.getDockerImageName())) {
                createOrUpdate(containerDto);
            }
        });
    }

    public void createOrUpdate(ContainerDto containerDto) {
        containerRepository.findByLanguageNameAndLanguageVersion(
                containerDto.getLanguageName(), containerDto.getLanguageVersion())
                .ifPresentOrElse(
                        container -> container.update(containerDto),
                        () -> containerRepository.save(containerMapper.INSTANCE.toEntity(containerDto))
                );
    }

    public List<ContainerDto> getAllContainers() {
        return myDockerClient.getAllContainers();
    }
}
