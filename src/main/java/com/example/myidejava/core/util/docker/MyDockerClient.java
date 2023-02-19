package com.example.myidejava.core.util.docker;

import com.example.myidejava.dto.docker.ContainerResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<ContainerResponse> getAllContainers() {
        List<ContainerResponse> containerResponseList = new ArrayList<>();
        DockerClient dockerClient = getDockerClient();

        dockerClient.listContainersCmd().withShowAll(true).exec().stream()
                .filter(container -> container.getImage().contains("container"))
                .forEach(container -> containerResponseList.add(ContainerResponse.containerToDto(container)));
        return containerResponseList;
    }

}
