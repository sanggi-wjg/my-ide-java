package com.example.myidejava.module.docker.executor;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.module.docker.MyDockerClient;
import com.example.myidejava.module.util.FileUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public abstract class ContainerCodeExecutor extends MyDockerClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String WORKDIR = "/app";

    public abstract CodeResponse execute(Container container, CodeRequest codeRequest);

    protected final File copyResourceToContainer(String containerId, String extension, String content) {
        File tempFile = FileUtil.createTemporaryFile(extension, content);
        DockerClient dockerClient = getDockerClient();
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withHostResource(tempFile.getAbsolutePath())
                .withRemotePath(WORKDIR)
                .exec();
        FileUtil.unlink(tempFile);
        return tempFile;
    }

    protected final Map<String, String> createAndStartCommand(String containerId, String[] command) {
        ExecCreateCmdResponse createCmdResponse = createCommand(containerId, command);
        return startCommand(createCmdResponse.getId());
    }

    protected final ExecCreateCmdResponse createCommand(String containerId, String[] command) {
        DockerClient dockerClient = getDockerClient();
        return dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(false)
                .withCmd(command)
                .exec();
    }

    protected final Map<String, String> startCommand(String createCommandResponseId) {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        ExecStartResultCallback cmdCallback = new ExecStartResultCallback(stdout, stderr);

        try {
            DockerClient dockerClient = getDockerClient();
            dockerClient.execStartCmd(createCommandResponseId)
                    .withDetach(false)
                    .exec(cmdCallback)
                    .awaitCompletion()
                    .onComplete();
        } catch (InterruptedException e) {
            throw new DockerAppException(ErrorCode.DOCKER_CONTAINER_FAIL_TO_START_COMMAND);
        }
        return resultStringToMap(stdout, stderr);
    }

    protected Map<String, String> resultStringToMap(ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) {
        Map<String, String> map = new HashMap<>();
        map.put("output", stdout.toString(StandardCharsets.UTF_8).strip());
        map.put("error", stderr.toString(StandardCharsets.UTF_8).strip());
        return map;
    }
}


