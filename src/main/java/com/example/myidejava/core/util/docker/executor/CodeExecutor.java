package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.fasterxml.jackson.databind.JsonNode;

public interface CodeExecutor {

    JsonNode execute(Container container, String code);

}


