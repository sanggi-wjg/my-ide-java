package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.module.docker.MyDockerClient;

public abstract class ContainerCodeExecutor extends MyDockerClient {

    public abstract CodeResponse execute(Container container, CodeRequest codeRequest);

}


