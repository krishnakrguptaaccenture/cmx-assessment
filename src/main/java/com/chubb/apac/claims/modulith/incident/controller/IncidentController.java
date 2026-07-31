package com.chubb.apac.claims.modulith.incident.controller;
import com.chubb.apac.claims.modulith.common.dto.ApiResponse;
import com.chubb.apac.claims.modulith.common.security.CurrentUser;
import com.chubb.apac.claims.modulith.common.util.CorrelationId;
import com.chubb.apac.claims.modulith.incident.dto.request.*;
import com.chubb.apac.claims.modulith.incident.dto.response.*;
import com.chubb.apac.claims.modulith.incident.service.IncidentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/incidents") @PreAuthorize("hasRole('CLAIMANT')")
public class IncidentController {
 private final IncidentService service; public IncidentController(IncidentService service){this.service=service;}
 @PostMapping public ResponseEntity<ApiResponse<IncidentResponse>> report(@AuthenticationPrincipal CurrentUser user,@RequestHeader(value=CorrelationId.HEADER,required=false) String correlationId,@Valid @RequestBody CreateIncidentRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.report(user.userId(),correlationId,request)));}
 @GetMapping("/{incidentId}") public ApiResponse<IncidentResponse> get(@AuthenticationPrincipal CurrentUser user,@PathVariable String incidentId){return ApiResponse.success(service.getOwned(user.userId(),incidentId));}
 @PutMapping("/{incidentId}") public ApiResponse<IncidentResponse> update(@AuthenticationPrincipal CurrentUser user,@PathVariable String incidentId,@RequestHeader(value=CorrelationId.HEADER,required=false) String correlationId,@Valid @RequestBody UpdateIncidentRequest request){return ApiResponse.success(service.updateOwned(user.userId(),incidentId,correlationId,request));}
 @PostMapping("/{incidentId}/parties") public ResponseEntity<ApiResponse<IncidentPartyResponse>> addParty(@AuthenticationPrincipal CurrentUser user,@PathVariable String incidentId,@Valid @RequestBody CreateIncidentPartyRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.addParty(user.userId(),incidentId,request)));}
 @GetMapping("/{incidentId}/parties") public ApiResponse<List<IncidentPartyResponse>> parties(@AuthenticationPrincipal CurrentUser user,@PathVariable String incidentId){return ApiResponse.success(service.listParties(user.userId(),incidentId));}
}
