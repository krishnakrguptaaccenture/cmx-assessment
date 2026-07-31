package com.chubb.apac.claims.modulith.notification.event;

import com.chubb.apac.claims.modulith.inforequest.event.InformationRequestedEvent;
import com.chubb.apac.claims.modulith.notification.model.NotificationChannel;
import com.chubb.apac.claims.modulith.notification.service.NotificationService;
import java.time.*;import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationEventListenerTest {
    @Test void translatesInformationRequestedEvent(){
        NotificationService service=mock(NotificationService.class);
        NotificationEventListener listener=new NotificationEventListener(service);
        listener.onInformationRequested(new InformationRequestedEvent(
                "E1","CLM-1","IRQ-1",List.of("Invoice"),LocalDate.now(),
                "S1",Instant.now(),"C1"));
        verify(service).create(any(),eq(NotificationChannel.IN_APP));
    }
}
