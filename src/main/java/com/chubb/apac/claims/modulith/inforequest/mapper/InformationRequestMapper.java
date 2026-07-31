package com.chubb.apac.claims.modulith.inforequest.mapper;
import com.chubb.apac.claims.modulith.inforequest.dto.response.*;
import com.chubb.apac.claims.modulith.inforequest.model.*;
import java.util.List;
import org.springframework.stereotype.Component;
@Component
public class InformationRequestMapper {
    public InformationRequestResponse toResponse(InformationRequest r,List<InformationResponse> responses){
        return new InformationRequestResponse(r.getRequestId(),r.getClaimId(),List.copyOf(r.getRequestedFields()),
                r.getDueDate(),r.getStatus(),r.getInstructions(),r.getCreatedAt(),r.getRequestedBy(),
                responses.stream().map(this::toSummary).toList());
    }
    public InformationResponseSummary toSummary(InformationResponse r){return new InformationResponseSummary(r.getResponseId(),r.getResponse(),r.getSubmittedAt());}
    public InformationResponseResponse toResponse(InformationResponse r){return new InformationResponseResponse(r.getResponseId(),r.getRequestId(),r.getClaimId(),r.getResponse(),r.getSubmittedAt());}
}
