package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import org.springframework.stereotype.Component;

@Component
public class CodeExecutorFactory {

    public ContainerCodeExecutor create(Container container) {
        switch (container.getCodeExecutorType()) {
            case HTTP -> {
                return new HttpCodeExecutor();
            }
            case PYTHON_DOCKER_EXEC -> {
                return new PythonDockerExecCodeExecutor();
            }
            case PHP_DOCKER_EXEC -> {
                return null;
            }
            default -> {
                throw new IllegalStateException("code executor factory is not implemented");
            }
        }
    }
}
