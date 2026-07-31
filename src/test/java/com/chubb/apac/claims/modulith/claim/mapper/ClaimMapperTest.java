package com.chubb.apac.claims.modulith.claim.mapper;
import com.chubb.apac.claims.modulith.claim.model.Claim;import com.chubb.apac.claims.modulith.common.enums.*;import org.junit.jupiter.api.Test;import static org.assertj.core.api.Assertions.assertThat;
class ClaimMapperTest {@Test void mapsClaim(){Claim c=new Claim();c.setClaimId("CLM-1");c.setIncidentId("INC-1");c.setStatus(ClaimStatus.REPORTED);c.setProductType(ProductType.MOTOR);c.setMarket(Market.SG);assertThat(new ClaimMapper().toResponse(c).claimId()).isEqualTo("CLM-1");}}
