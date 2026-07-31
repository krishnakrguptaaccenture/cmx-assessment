package com.chubb.apac.claims.modulith.user.mapper;
import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.user.model.User;
import org.junit.jupiter.api.Test;import static org.assertj.core.api.Assertions.*;
class UserMapperTest { @Test void mapsUser(){
    User u=new User();u.setId("U1");u.setEmail("a@b.com");u.setFullName("A");u.getRoles().add(UserRole.CLAIMANT);var r=new UserMapper().toResponse(u);assertThat(r.userId()).isEqualTo("U1");assertThat(r.roles()).containsExactly(UserRole.CLAIMANT);}}
