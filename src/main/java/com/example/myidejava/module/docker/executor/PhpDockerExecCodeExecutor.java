package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@Component
public class PhpDockerExecCodeExecutor extends ContainerCodeExecutor {

    @Override
    public CodeResponse execute(Container container, CodeRequest codeRequest) {
        // todo refactoring
        File sourceFile = createTemporaryFile("php", codeRequest.getCode());
        copyResourceToContainer(container.getContainerId(), sourceFile);

        String[] command = {"php", "/app/" + sourceFile.getName()};
        ExecCreateCmdResponse createCmdResponse = createCommand(container.getContainerId(), command);
        Map<String, String> resultMap = startCommand(createCmdResponse.getId());

        return CodeResponse.builder()
                .output(resultMap.get("output"))
                .error(resultMap.get("error"))
                .build();
    }
}
