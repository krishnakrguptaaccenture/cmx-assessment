package com.chubb.apac.claims.modulith.inforequest.service;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.inforequest.dto.request.*;
import com.chubb.apac.claims.modulith.inforequest.dto.response.*;
import java.util.*;
public interface InformationRequestService {
 InformationRequestResponse create(String claimId,String staffId,Set<Market> markets,String correlationId,CreateInformationRequest request);
 List<InformationRequestResponse> listForStaff(String claimId,String staffId,Set<Market> markets);
 List<InformationRequestResponse> listForClaimant(String claimId,String claimantId);
 InformationResponseResponse submit(String claimId,String requestId,String claimantId,String correlationId,SubmitInformationResponse request);
}
