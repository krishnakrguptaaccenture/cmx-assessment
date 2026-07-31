package com.chubb.apac.claims.modulith.user.controller;

import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.user.dto.response.AuthResponse;
import com.chubb.apac.claims.modulith.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

 @Autowired
 private MockMvc mvc;

 @MockBean
 private AuthService service;

 @Test
 void registerReturns201() throws Exception {

  AuthResponse response =
          new AuthResponse(
                  "token",
                  "u",
                  "a@b.com",
                  "Alex",
                  UserRole.CLAIMANT
          );

  when(service.register(any()))
          .thenReturn(response);

  mvc.perform(
                  post("/api/v1/auth/register")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content("""
                                        {
                                          "email":"a@b.com",
                                          "password":"password1",
                                          "fullName":"Alex"
                                        }
                                        """))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.data.userId").value("u"));
 }

 @Test
 void loginReturns200() throws Exception {

  AuthResponse response =
          new AuthResponse(
                  "token",
                  "u",
                  "a@b.com",
                  "Alex",
                  UserRole.CLAIMANT
          );

  when(service.login(any()))
          .thenReturn(response);

  mvc.perform(
                  post("/api/v1/auth/login")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content("""
                                        {
                                          "email":"a@b.com",
                                          "password":"password1"
                                        }
                                        """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.userId").value("u"));
 }

 @Test
 void logoutReturns204() throws Exception {

  doNothing().when(service)
          .logout(eq("Bearer token"));

  mvc.perform(
                  post("/api/v1/auth/logout")
                          .header("Authorization", "Bearer token"))
          .andExpect(status().isNoContent());

  verify(service)
          .logout("Bearer token");
 }
}