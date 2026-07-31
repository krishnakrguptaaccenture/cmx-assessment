package com.chubb.apac.claims.modulith.incident.service;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.exception.*;
import com.chubb.apac.claims.modulith.incident.dto.request.*;
import com.chubb.apac.claims.modulith.incident.event.IncidentReportedEvent;
import com.chubb.apac.claims.modulith.incident.mapper.IncidentMapper;
import com.chubb.apac.claims.modulith.incident.model.*;
import com.chubb.apac.claims.modulith.incident.repository.*;
import com.chubb.apac.claims.modulith.incident.service.impl.IncidentServiceImpl;
import java.time.Instant;import java.util.*;
import org.junit.jupiter.api.*;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.context.ApplicationEventPublisher;
import static org.assertj.core.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {
 @Mock IncidentRepository incidents; @Mock IncidentPartyRepository parties; @Mock ApplicationEventPublisher events; @Mock IncidentIdGenerator ids;
 IncidentServiceImpl service;
 @BeforeEach void setUp(){service=new IncidentServiceImpl(incidents,parties,new IncidentMapper(),ids,events);}
 @Test void reportsOwnedIncidentAndPublishesEvent(){when(ids.nextIncidentId()).thenReturn("INC-1");when(incidents.saveAndFlush(any())).thenAnswer(x->x.getArgument(0));var req=new CreateIncidentRequest(Instant.now(),IncidentType.MOTOR_ACCIDENT,"Singapore","Collision",Market.SG,List.of(new ClaimItemRequest(ClaimItemType.MOTOR_VEHICLE,"Car",null)));var out=service.report("U1","corr-1",req);assertThat(out.incidentId()).isEqualTo("INC-1");assertThat(out.claimantId()).isEqualTo("U1");verify(events).publishEvent(any(IncidentReportedEvent.class));}
 @Test void hidesAnotherClaimantsIncidentAsNotFound(){when(incidents.findByIncidentIdAndClaimantId("INC-1","U2")).thenReturn(Optional.empty());assertThatThrownBy(()->service.getOwned("U2","INC-1")).isInstanceOf(ResourceNotFoundException.class);}
 @Test void rejectsUpdateAfterReviewStarts(){Incident i=new Incident();i.setIncidentId("INC-1");i.setClaimantId("U1");i.setStatus(IncidentStatus.UNDER_REVIEW);when(incidents.findByIncidentIdAndClaimantId("INC-1","U1")).thenReturn(Optional.of(i));assertThatThrownBy(()->service.updateOwned("U1","INC-1","corr",new UpdateIncidentRequest("new",null))).isInstanceOf(ConflictException.class);}
}
