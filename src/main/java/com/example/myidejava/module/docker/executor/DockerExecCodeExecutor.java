package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class DockerExecCodeExecutor extends ContainerCodeExecutor {

    @Override
    public CodeResponse execute(Container container, CodeRequest codeRequest) {
        // todo : refactoring
        String[] command = {"python", "/app/app.py", codeRequest.getCode()};

        DockerClient dockerClient = getDockerClient();
        ExecCreateCmdResponse cmdResponse = dockerClient.execCreateCmd(container.getContainerId())
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(false)
                .withCmd(command)
                .exec();

        String cmdStdout;
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        try {
            ExecStartResultCallback cmdCallback = new ExecStartResultCallback(stdout, stderr);
            dockerClient.execStartCmd(cmdResponse.getId())
                    .withDetach(false)
                    .exec(cmdCallback)
                    .awaitCompletion()
                    .onComplete();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cmdStdout = stdout.toString(StandardCharsets.UTF_8).strip();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        Map<String, String> map;
        try {
            map = mapper.readValue(cmdStdout, Map.class);
            System.out.println(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CodeResponse.builder()
                .output(map.get("output"))
                .error(map.get("error"))
                .build();
    }
}
