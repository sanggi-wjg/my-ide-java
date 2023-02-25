package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.core.util.docker.MyDockerClient;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.springframework.stereotype.Component;

//@Component
public class DockerExecCodeExecutor implements CodeExecutor {
    MyDockerClient myDockerClient;

    @Override
    public CodeResponse execute(Container container, String code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("python /app/app.py ");
        stringBuilder.append(code);

        DockerClient dockerClient = myDockerClient.getDockerClient();
        ExecCreateCmdResponse exec = dockerClient.execCreateCmd(container.getContainerId()).withCmd(stringBuilder.toString()).exec();

        return null;
    }
}
