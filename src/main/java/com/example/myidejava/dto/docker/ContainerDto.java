package com.example.myidejava.dto.docker;

import com.github.dockerjava.api.model.Container;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "컨테이너 Response")
public class ContainerDto {
    @Schema(description = "컨테이너 ID", defaultValue = "1")
    private Long id;

    @Schema(description = "도커 컨테이너 ID", defaultValue = "6539e3109c4ea65371e62ff62f0ef382db13c56616ec7ba96c9cec1e559c6a31")
    private String containerId;

    @Schema(description = "도커 이미지 이름", defaultValue = "docker-python-3.8")
    private String dockerImageName;

    @Schema(description = "언어 이름", defaultValue = "python")
    private String languageName;

    @Schema(description = "언어 버전", defaultValue = "3.8")
    private String languageVersion;

    @Schema(description = "도커 컨테이너 Up/Down 상태", defaultValue = "Up 3 days")
    private String containerStatus;

    @Schema(description = "도커 컨테이너 상태", defaultValue = "running")
    private String containerState;

    @Schema(description = "도커 컨테이너 Open Port", defaultValue = "8000")
    private Map<String, Object> containerPorts;

    public String getLanguageName() {
        return getDockerImageName().split("-")[1];
    }

    public String getLanguageVersion() {
        return getDockerImageName().split("-")[2];
    }

    public static ContainerDto containerToDto(Container container) {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(container.getPorts()).forEach(
                port -> map.put(
                        port.getIp() == null ? "localhost" : port.getIp(),
                        port.getPrivatePort()
                ));
        return ContainerDto.builder()
                .containerId(container.getId())
                .dockerImageName(container.getImage())
                .containerState(container.getState())
                .containerStatus(container.getStatus())
                .containerPorts(map)
                .build();
    }

}
