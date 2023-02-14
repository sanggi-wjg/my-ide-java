package com.example.myidejava.core.listeners;

import com.example.myidejava.service.docker.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private final ContainerService containerService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("=================================================================");
        System.out.println("[STARTED Listener]");
        System.out.println("Application Started");
        containerService.initialize();
        System.out.println("=================================================================");
    }

}