package com.chubb.apac.claims.modulith.notification.event;

import com.chubb.apac.claims.modulith.assessment.event.DecisionMadeEvent;
import com.chubb.apac.claims.modulith.inforequest.event.*;
import com.chubb.apac.claims.modulith.notification.dto.NotificationCommand;
import com.chubb.apac.claims.modulith.notification.model.*;
import com.chubb.apac.claims.modulith.notification.service.NotificationService;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.*;

@Component
public class NotificationEventListener {
    private final NotificationService notifications;
    public NotificationEventListener(NotificationService notifications){this.notifications=notifications;}

    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void onInformationRequested(InformationRequestedEvent event){
        notifications.create(new NotificationCommand(
                NotificationEventType.INFORMATION_REQUESTED,
                event.claimId(),event.occurredAt(),event.correlationId(),
                Map.of("claimId",event.claimId(),"requestId",event.requestId(),
                        "dueDate",event.dueDate().toString())),NotificationChannel.IN_APP);
    }

    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void onInformationSubmitted(InformationSubmittedEvent event){
        notifications.create(new NotificationCommand(
                NotificationEventType.INFORMATION_SUBMITTED,
                event.claimId(),event.occurredAt(),event.correlationId(),
                Map.of("claimId",event.claimId(),"requestId",event.requestId())),
                NotificationChannel.IN_APP);
    }

    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void onDecisionMade(DecisionMadeEvent event){
        notifications.create(new NotificationCommand(
                NotificationEventType.DECISION_MADE,
                event.claimId(),event.decisionDate(),event.correlationId(),
                Map.of("claimId",event.claimId(),"decision",event.decision().name())),
                NotificationChannel.IN_APP);
    }
}
