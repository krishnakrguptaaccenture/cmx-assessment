package com.chubb.apac.claims.modulith.inforequest.controller;
import com.chubb.apac.claims.modulith.common.dto.ApiResponse;
import com.chubb.apac.claims.modulith.common.security.CurrentUser;
import com.chubb.apac.claims.modulith.common.util.CorrelationId;
import com.chubb.apac.claims.modulith.inforequest.dto.request.SubmitInformationResponse;
import com.chubb.apac.claims.modulith.inforequest.dto.response.*;
import com.chubb.apac.claims.modulith.inforequest.service.InformationRequestService;
import jakarta.validation.Valid;import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/claims/{claimId}/info-requests") @PreAuthorize("hasRole('CLAIMANT')")
public class ClaimantInformationRequestController {
 private final InformationRequestService service;public ClaimantInformationRequestController(InformationRequestService service){this.service=service;}
 @GetMapping public ApiResponse<List<InformationRequestResponse>> list(@AuthenticationPrincipal CurrentUser user,@PathVariable String claimId){return ApiResponse.success(service.listForClaimant(claimId,user.userId()));}
 @PostMapping("/{requestId}/submit") public ApiResponse<InformationResponseResponse> submit(@AuthenticationPrincipal CurrentUser user,@PathVariable String claimId,@PathVariable String requestId,@RequestHeader(value=CorrelationId.HEADER,required=false) String correlationId,@Valid @RequestBody SubmitInformationResponse request){return ApiResponse.success(service.submit(claimId,requestId,user.userId(),correlationId,request));}
}
