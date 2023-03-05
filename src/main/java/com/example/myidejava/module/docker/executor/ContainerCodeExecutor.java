package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.module.docker.MyDockerClient;
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
import java.util.UUID;

@Component
@Slf4j
public abstract class ContainerCodeExecutor extends MyDockerClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String WORKDIR = "/app";

    public abstract CodeResponse execute(Container container, CodeRequest codeRequest);

    protected final String generateUniqueFilename(String fileExtension) {
        // todo file path format join
        return UUID.randomUUID() + "." + fileExtension;
    }

    protected final File createTemporaryFile(String extension, String content) {
        String filename = generateUniqueFilename(extension);
        File file = new File(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("todo, " + e.getMessage());
        }
        return file;
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

    protected final void copyResourceToContainer(String containerId, File file) {
        DockerClient dockerClient = getDockerClient();
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withHostResource(file.getAbsolutePath())
                .withRemotePath(WORKDIR)
                .exec();
        unlink(file);
    }

    protected final void unlink(File file) {
        if (file.exists()) {
            file.delete();
        }
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
            throw new RuntimeException(e); // todo
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


