package com.example.myidejava.core.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyDockerClient {
    private final Logger logger = LoggerFactory.getLogger(MyDockerClient.class);

    public void getContainers() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();

        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
//                .connectionTimeout(Duration.ofSeconds(10))
//                .responseTimeout(Duration.ofSeconds(20))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd().withShowAll(true);
        for (Container container : listContainersCmd.exec()) {
            System.out.println(container.getId());
        }
    }

}
