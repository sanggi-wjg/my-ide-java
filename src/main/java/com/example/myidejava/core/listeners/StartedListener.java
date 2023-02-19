package com.example.myidejava.core.listeners;

import com.example.myidejava.service.docker.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ContainerService containerService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("[Started Listener] =================================================================");
        logger.info("[Started Listener] Initialize Containers");
        containerService.initialize();
        logger.info("[Started Listener] =================================================================");
    }

}