package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.core.util.docker.MyDockerClient;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.AttachContainerCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DockerExecCodeExecutor implements CodeExecutor {

    private final MyDockerClient myDockerClient;

    @Override
    public CodeResponse execute(Container container, String code) {
        DockerClient dockerClient = myDockerClient.getDockerClient();
        AttachContainerCmd attachContainerCmd = dockerClient.attachContainerCmd(container.getContainerId());
//        attachContainerCmd.exec(callback -> {
//            System.out.println(callback);
//            System.out.println(callback);
//        });
        return null;
    }
}
