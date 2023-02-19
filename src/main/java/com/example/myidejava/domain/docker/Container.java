package com.example.myidejava.domain.docker;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public boolean isTypeDockerExec() {
        return codeExecutorType.equals(CodeExecutorType.DOCKER_EXEC);
    }

    public boolean isTypeHttp() {
        return codeExecutorType.equals(CodeExecutorType.HTTP);
    }

    public String getHttpUrlAddress() {
        if (!isTypeHttp()) {
            throw new IllegalStateException("todo : http type 이 아님");
        }
        return "http://" + containerPorts.entrySet().stream().map(m -> m.getKey() + ":" + m.getValue() + "/run").collect(Collectors.joining());
//        return Joiner.on(":").withKeyValueSeparator(":").join(containerPorts);
    }

    public void saveCodeExecutorType() {
        codeExecutorType = getContainerPorts().isEmpty() ? CodeExecutorType.DOCKER_EXEC : CodeExecutorType.HTTP;
    }

    public void saveContainerInfo(ContainerResponse containerResponse) {
        dockerImageName = containerResponse.getDockerImageName();
        containerId = containerResponse.getContainerId();
        containerState = containerResponse.getContainerState();
        containerStatus = containerResponse.getContainerStatus();
        containerPorts = containerResponse.getContainerPorts();
        saveCodeExecutorType();
    }
}
