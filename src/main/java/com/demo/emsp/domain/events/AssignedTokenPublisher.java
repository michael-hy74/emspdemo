package com.demo.emsp.domain.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AssignedTokenPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public AssignedTokenPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(Object event) {
        eventPublisher.publishEvent(event);
    }
}
