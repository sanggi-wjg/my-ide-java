package com.example.myidejava.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("app")
public class AppProperty {
    private List<String> dockerImageNames;

    public boolean isContainDockerImageName(String dockerImageName) {
        return dockerImageNames.contains(dockerImageName);
    }
}
