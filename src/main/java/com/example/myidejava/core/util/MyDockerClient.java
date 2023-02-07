package com.example.myidejava.core.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MyDockerClient {
    @Value("${DOCKER_HOST}")
    private String dockerHost;

//    public MyDockerClient() {
//        DefaultDockerClientConfig configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost(this.DOCKER_HOST)
//                .build();
//    }

    public void getContainers() {
        DefaultDockerClientConfig configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(this.dockerHost)
                .withDockerTlsVerify(false)
                .build();

        System.out.println(this.dockerHost);
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        List<Container> containers = dockerClient.listContainersCmd().exec();
        containers.forEach(System.out::println);
    }

}
