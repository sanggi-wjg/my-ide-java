package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.domain.docker.Container;
import org.springframework.stereotype.Component;

@Component
public class CodeExecutorFactory {

    public CodeExecutor create(Container container) {
        if (container.isTypeDockerExec()) {
            return new HttpCodeExecutor();
        }
        return new HttpCodeExecutor();
    }


}
