package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class GccDockerExecCodeExecutor extends ContainerCodeExecutor {

    @Override
    public CodeResponse execute(Container container, CodeRequest codeRequest) {
        // todo 컨테이너로 파일 copy 했으니 container 안에 파일 지우는 쉘 스크립트나 지우는 로직 추가 필요
        String filename = copyResourceToContainer(container.getContainerId(), "c", codeRequest.getCode());

        // todo refactoring : 커맨드랑 파일 이름들 지저분 함
        String gccCompileFilename = "/app/" + filename.toUpperCase(Locale.ROOT).replace(".C", "");
        String[] gccCommand = {"gcc", "-o", gccCompileFilename, "/app/" + filename};
        ExecCreateCmdResponse createGccCmdResponse = createCommand(container.getContainerId(), gccCommand);
        startCommand(createGccCmdResponse.getId());

        String[] command = {gccCompileFilename};
        ExecCreateCmdResponse createCmdResponse = createCommand(container.getContainerId(), command);
        Map<String, String> resultMap = startCommand(createCmdResponse.getId());

        return CodeResponse.builder()
                .output(resultMap.get("output"))
                .error(resultMap.get("error"))
                .build();
    }
}
