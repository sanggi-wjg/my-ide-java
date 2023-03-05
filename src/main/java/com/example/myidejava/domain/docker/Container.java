package com.example.myidejava.domain.docker;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.common.BaseDateTime;
import com.example.myidejava.dto.docker.ContainerResponse;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "container", uniqueConstraints = {
        @UniqueConstraint(name = "uq_container_language_version", columnNames = {"language_name", "language_version"})
})
public class Container extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "container_id", length = 100, nullable = false)
    private String containerId;

    @Column(name = "docker_image_name", length = 100, nullable = false)
    private String dockerImageName;

    @Column(name = "language_name", length = 100, nullable = false)
    private String languageName;

    @Column(name = "language_version", length = 100, nullable = false)
    private String languageVersion;

    @Column(name = "container_status", length = 100, nullable = false)
    private String containerStatus;

    @Column(name = "container_state", length = 100, nullable = false)
    private String containerState;

    @Type(JsonType.class)
    @Column(name = "container_ports", columnDefinition = "json")
    private Map<String, Object> containerPorts = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "code_executor_type")
    private CodeExecutorType codeExecutorType;

    @OneToMany(mappedBy = "container")
    private List<CodeSnippet> codeSnippetList = new ArrayList<>();

    public boolean isTypeHttp() {
        return codeExecutorType.equals(CodeExecutorType.HTTP);
    }

    public String getHttpUrlAddress() {
        if (!isTypeHttp()) {
            throw new DockerAppException(ErrorCode.DOCKER_CONTAINER_IS_NOT_HTTP_TYPE);
        }
        return "http://" + containerPorts.entrySet().stream().map(m -> m.getKey() + ":" + m.getValue()).collect(Collectors.joining());
    }

    public void saveCodeExecutorType() {
        switch (languageName) {
            case "python" -> {
                if (languageVersion.equals("3.8")) {
                    codeExecutorType = CodeExecutorType.HTTP;
                } else {
                    codeExecutorType = CodeExecutorType.PYTHON_DOCKER_EXEC;
                }
            }
            case "php" -> codeExecutorType = CodeExecutorType.PHP_DOCKER_EXEC;
            case "gcc" -> codeExecutorType = CodeExecutorType.GCC_DOCKER_EXEC;
        }
    }

    public void saveContainerInfo(ContainerResponse containerResponse) {
        containerId = containerResponse.getContainerId();
        dockerImageName = containerResponse.getDockerImageName();
        languageName = containerResponse.getLanguageName();
        languageVersion = containerResponse.getLanguageVersion();
        containerStatus = containerResponse.getContainerStatus();
        containerState = containerResponse.getContainerState();
        containerPorts = containerResponse.getContainerPorts();
        saveCodeExecutorType();
    }
}
