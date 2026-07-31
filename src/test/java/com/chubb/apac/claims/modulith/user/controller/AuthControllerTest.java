package com.chubb.apac.claims.modulith.user.controller;
import com.chubb.apac.claims.modulith.common.exception.GlobalExceptionHandler;import com.chubb.apac.claims.modulith.user.dto.response.AuthResponse;import com.chubb.apac.claims.modulith.user.model.UserRole;import com.chubb.apac.claims.modulith.user.service.AuthService;import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.boot.test.mock.mockito.MockBean;import org.springframework.http.MediaType;import org.springframework.test.web.servlet.MockMvc;import static org.mockito.ArgumentMatchers.any;import static org.mockito.Mockito.when;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers=AuthController.class) @AutoConfigureMockMvc(addFilters=false) class AuthControllerTest {@Autowired MockMvc mvc;@MockBean AuthService service;
 @Test
 void registerReturns201() throws Exception {

  when(service.register(any()))
          .thenReturn(
                  new AuthResponse(
                          "t",
                          "u",
                          "a@b.com",
                          "Alex",
                          UserRole.CLAIMANT));

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
 } @Test void invalidRegisterReturns400()throws Exception{mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());}}
