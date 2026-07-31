package com.chubb.apac.claims.modulith.inforequest.controller;
import com.chubb.apac.claims.modulith.common.dto.ApiResponse;
import com.chubb.apac.claims.modulith.common.security.CurrentUser;
import com.chubb.apac.claims.modulith.common.util.CorrelationId;
import com.chubb.apac.claims.modulith.inforequest.dto.request.CreateInformationRequest;
import com.chubb.apac.claims.modulith.inforequest.dto.response.InformationRequestResponse;
import com.chubb.apac.claims.modulith.inforequest.service.InformationRequestService;
import jakarta.validation.Valid;import java.util.List;import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/staff/claims/{claimId}/info-requests") @PreAuthorize("hasRole('CLAIMS_STAFF')")
public class StaffInformationRequestController {
 private final InformationRequestService service;public StaffInformationRequestController(InformationRequestService service){this.service=service;}
 @PostMapping public ResponseEntity<ApiResponse<InformationRequestResponse>> create(@AuthenticationPrincipal CurrentUser user,@PathVariable String claimId,@RequestHeader(value=CorrelationId.HEADER,required=false) String correlationId,@Valid @RequestBody CreateInformationRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.create(claimId,user.userId(),user.markets(),correlationId,request)));}
 @GetMapping public ApiResponse<List<InformationRequestResponse>> list(@AuthenticationPrincipal CurrentUser user,@PathVariable String claimId){return ApiResponse.success(service.listForStaff(claimId,user.userId(),user.markets()));}
}
