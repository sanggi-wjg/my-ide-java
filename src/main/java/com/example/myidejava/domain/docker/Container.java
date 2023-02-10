package com.example.myidejava.domain.docker;

import com.example.myidejava.domain.common.BaseDateTime;
import com.example.myidejava.dto.docker.ContainerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "container_state", length = 100, nullable = false)
    private String containerState;

    @Column(name = "language_name", length = 100, nullable = false)
    private String languageName;

    @Column(name = "language_version", length = 100, nullable = false)
    private String languageVersion;

    public void update(ContainerDto containerDto){
        this.containerId = containerDto.getContainerId();
        this.containerState = containerDto.getContainerState();
    }
}
