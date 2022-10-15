package com.example.challenge2.presentation;

import com.example.challenge2.application.ExternalAPIService;
import com.example.challenge2.presentation.dto.ExternalApiCallResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/external")
public class ExternalAPIController {
  private final ExternalAPIService service;

  public ExternalAPIController(ExternalAPIService service) {
    this.service = service;
  }

  @GetMapping("/{param}")
  @Operation(summary = "calls external API service with given request param")
  public ExternalApiCallResponse callExternalAPI(@PathVariable String param) {
    return service.getExternalApiResponse(param);
  }
}
