package com.example.myidejava.core.util.docker;

import com.example.myidejava.dto.docker.ContainerResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyDockerClient {

    protected DockerClient getDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
//                .connectionTimeout(Duration.ofSeconds(10))
//                .responseTimeout(Duration.ofSeconds(20))
                .build();
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
