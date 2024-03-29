package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Locale;
import java.util.Map;

@Component
public class GccDockerExecCodeExecutor extends ContainerCodeExecutor {

    private String getGccCompileFilename(String filename) {
        return "/app/" + filename.replace(".c", "").toUpperCase(Locale.ROOT);
    }

    @Override
    public CodeResponse execute(Container container, String codeRequest) {
        // todo 컨테이너로 파일 copy 했으니 container 안에 파일 지우는 쉘 스크립트나 지우는 로직 추가 필요
        File file = copyResourceToContainer(container.getContainerId(), "c", codeRequest);

        String gccCompileFilename = getGccCompileFilename(file.getName());
        String[] gccCommand = {"gcc", "-o", gccCompileFilename, "/app/" + file.getName()};
        createAndStartCommand(container.getContainerId(), gccCommand);

        String[] command = {gccCompileFilename};
        Map<String, String> result = createAndStartCommand(container.getContainerId(), command);

        return CodeResponse.builder()
                .output(result.get("output"))
                .error(result.get("error"))
                .build();
    }
}
