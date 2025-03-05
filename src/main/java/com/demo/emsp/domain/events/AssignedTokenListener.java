package com.demo.emsp.domain.events;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AssignedTokenListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAssignedTokenEvent(AssignedTokenEvent event) {
        // this is mock for finish assign token and event be invoked and run.
        System.out.println("Send Message：Token " + event.getTokenId().getValue() +
                " has assigned to Account " + event.getAccountId().getValue());
    }
}
