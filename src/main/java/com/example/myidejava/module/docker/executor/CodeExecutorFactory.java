package com.example.myidejava.module.docker.executor;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
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
                return new PhpDockerExecCodeExecutor();
            }
            case GCC_DOCKER_EXEC -> {
                return new GccDockerExecCodeExecutor();
            }
            case GO_DOCKER_EXEC -> {
                return new GoDockerExecCodeExecutor();
            }
            default -> throw new DockerAppException(ErrorCode.DOCKER_CONTAINER_CODE_EXECUTOR_IS_NOT_IMPLEMENTED);
        }
    }
}
