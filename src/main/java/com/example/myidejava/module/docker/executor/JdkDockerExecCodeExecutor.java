package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@Component
public class JdkDockerExecCodeExecutor extends ContainerCodeExecutor {

    @Override
    public CodeResponse execute(Container container, String codeRequest) {
        File file = copyResourceToContainer(container.getContainerId(), "java", codeRequest);

        String[] compileCommand = {"javac", "/app/" + file.getName()};
        createAndStartCommand(container.getContainerId(), compileCommand);

        String[] command = {"java", "/app/" + file.getName()};
        Map<String, String> result = createAndStartCommand(container.getContainerId(), command);

        return CodeResponse.builder()
                .output(result.get("output"))
                .error(result.get("error"))
                .build();
    }
}
