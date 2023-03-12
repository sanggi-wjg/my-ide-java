package com.example.myidejava.module.docker;

import com.example.myidejava.dto.docker.ContainerResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DockerClientShortCut extends MyDockerClient {
    private static final String IDE_CONTAINER_NAME = "container";

    public List<ContainerResponse> getAllContainers() {
        List<ContainerResponse> containerResponses = new ArrayList<>();
        DockerClient dockerClient = getDockerClient();

        dockerClient.listContainersCmd().withShowAll(true).exec().stream()
                .filter(container -> container.getImage().contains(IDE_CONTAINER_NAME))
                .forEach(container -> containerResponses.add(ContainerResponse.containerToDto(container)));
        return containerResponses;
    }

    public InspectContainerResponse inspectContainer(String containerId) {
        DockerClient dockerClient = getDockerClient();
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

    public boolean isContainerStateRunning(String containerId) {
        return Boolean.TRUE.equals(inspectContainer(containerId).getState().getRunning());
    }

}
