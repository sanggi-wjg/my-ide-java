package com.example.myidejava.module.docker.executor;

import com.example.myidejava.core.exception.error.UtilException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class PythonDockerExecCodeExecutor extends ContainerCodeExecutor {

    @Override
    public CodeResponse execute(Container container, CodeRequest codeRequest) {
        // todo : refactoring
        String[] command = {"python", "/app/app.py", codeRequest.getCode()};

        ExecCreateCmdResponse createCmdResponse = createCommand(container.getContainerId(), command);
        Map<String, String> resultMap = startCommand(createCmdResponse.getId());

        return CodeResponse.builder()
                .output(resultMap.get("output"))
                .error(resultMap.get("error"))
                .build();
    }

    @Override
    protected Map<String, String> resultStringToMap(ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        Map<String, String> map;
        try {
            map = mapper.readValue(stdout.toString(StandardCharsets.UTF_8).strip(), Map.class);
        } catch (JsonProcessingException e) {
            throw new UtilException(ErrorCode.FAIL_TO_DECODE_JSON);
        }
        return map;
    }
}
