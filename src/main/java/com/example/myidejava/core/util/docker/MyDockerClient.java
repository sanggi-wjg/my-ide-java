package com.example.myidejava.core.util.docker;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.aopalliance.reflect.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MyDockerClient {

    public DockerClient getDockerClient() {
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

    public CodeResponse executeCode(Container container, CodeRequest codeRequest) {
        if (container.isTypeHttp()) {
            ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
            jsonNodes.put("code", codeRequest.getCode());

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(container.getHttpUrlAddress(), jsonNodes, JsonNode.class);

            JsonNode body = response.getBody();
            return CodeResponse.builder()
                    .output(body.get("output").textValue())
                    .error(body.get("error").textValue())
                    .build();

        } else if (container.isTypeDockerExec()) {
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
        } else {
            throw new IllegalStateException("code executor factory is not implemented");
        }
    }

}
