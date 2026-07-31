package com.chubb.apac.claims.modulith.claim.repository;
import com.chubb.apac.claims.modulith.claim.model.Claim;
import com.chubb.apac.claims.modulith.common.enums.*;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;
public final class ClaimSpecifications {
 private ClaimSpecifications(){}
 public static Specification<Claim> visibleTo(Set<Market> markets){return (r,q,b)->r.get("market").in(markets);}
 public static Specification<Claim> hasStatus(ClaimStatus status){return status==null?null:(r,q,b)->b.equal(r.get("status"),status);}
 public static Specification<Claim> hasMarket(Market market){return market==null?null:(r,q,b)->b.equal(r.get("market"),market);}
 public static Specification<Claim> hasProduct(ProductType product){return product==null?null:(r,q,b)->b.equal(r.get("productType"),product);}
 public static Specification<Claim> assignedTo(String staffId){return staffId==null||staffId.isBlank()?null:(r,q,b)->b.equal(r.get("assignedStaffId"),staffId);}
}
