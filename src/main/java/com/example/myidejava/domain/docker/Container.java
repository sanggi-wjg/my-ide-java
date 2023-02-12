package com.example.myidejava.domain.docker;

import com.example.myidejava.domain.common.BaseDateTime;
import com.example.myidejava.dto.docker.ContainerDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String containerPorts;

    public void saveContainerInfo(ContainerDto containerDto) {
        this.containerId = containerDto.getContainerId();
        this.containerState = containerDto.getContainerState();
        this.containerStatus = containerDto.getContainerStatus();
        this.containerPorts = containerDto.getContainerPorts();
    }
}
