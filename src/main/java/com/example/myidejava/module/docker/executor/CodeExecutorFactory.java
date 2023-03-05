package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import org.springframework.stereotype.Component;

@Component
public class CodeExecutorFactory {

    public ContainerCodeExecutor create(Container container) {
        // todo refactoring
        if (container.isTypeDockerExec()) {
            return new DockerExecCodeExecutor();

        } else if (container.isTypeHttp()) {
            return new HttpCodeExecutor();

        } else {
            throw new IllegalStateException("code executor factory is not implemented");
        }
    }


}
