package com.chubb.apac.claims.modulith.config.controller;
import com.chubb.apac.claims.modulith.common.dto.ApiResponse;import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.config.dto.*;import com.chubb.apac.claims.modulith.config.service.ConfigurationQueryService;import java.util.List;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/config")
public class ConfigurationController {
 private final ConfigurationQueryService service;public ConfigurationController(ConfigurationQueryService service){this.service=service;}
 @GetMapping("/markets") public ApiResponse<List<MarketConfigurationResponse>> markets(){return ApiResponse.success(service.markets());}
 @GetMapping("/products/{market}") public ApiResponse<List<ProductConfigurationResponse>> products(@PathVariable Market market){return ApiResponse.success(service.products(market));}
 @GetMapping("/rules/{market}/{product}") public ApiResponse<List<BusinessRuleResponse>> rules(@PathVariable Market market,@PathVariable("product") ProductType product){return ApiResponse.success(service.rules(market,product));}
}
