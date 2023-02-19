package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeResponse;

public interface CodeExecutor {

    CodeResponse execute(Container container, String code);

}


