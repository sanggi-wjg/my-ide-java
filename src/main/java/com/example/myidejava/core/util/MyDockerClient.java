package com.example.myidejava.core.util;

import com.example.myidejava.dto.docker.ContainerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MyDockerClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DefaultDockerClientConfig config;
    private final ApacheDockerHttpClient httpClient;

    public MyDockerClient() {
        config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
//                .connectionTimeout(Duration.ofSeconds(10))
//                .responseTimeout(Duration.ofSeconds(20))
                .build();
    }

    protected DockerClient getDockerClient() {
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public List<ContainerDto> getAllContainers() {
        List<ContainerDto> containerDtoList = new ArrayList<>();

        try (ListContainersCmd containersCmd = getDockerClient().listContainersCmd().withShowAll(true)) {
            containersCmd.exec().stream()
                    .filter(container -> container.getImage().contains("docker-"))
                    .forEach(container -> {
                                Map<String, Integer> map = new HashMap<>();
                                Arrays.stream(container.getPorts()).forEach(
                                        port -> map.put(
                                                port.getIp() == null ? "localhost" : port.getIp(),
                                                port.getPrivatePort()
                                        ));
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    containerDtoList.add(
                                            ContainerDto.builder()
                                                    .containerId(container.getId())
                                                    .dockerImageName(container.getImage())
                                                    .containerState(container.getState())
                                                    .containerStatus(container.getStatus())
                                                    .containerPorts(mapper.writeValueAsString(map))
                                                    .build());
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }
        return containerDtoList;
    }

}
