package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.module.docker.DockerClientShortCut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ContainerValidationService {
    private final DockerClientShortCut dockerClientShortCut;

    public void validateIsContainerRunning(Container container) {
        boolean isStateRunning = container.isStateRunning();
        boolean isContainerStateRunning = dockerClientShortCut.isContainerStateRunning(container.getContainerId());

        if (!isStateRunning || !isContainerStateRunning) {
            throw new DockerAppException(ErrorCode.DOCKER_CONTAINER_IS_NOT_RUNNING);
        }
    }

}
