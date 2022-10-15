package com.example.challenge2.application;

import com.example.challenge2.presentation.dto.ExternalApiCallResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExternalAPIServiceTest {
  @Mock RestTemplate restTemplate;

  @InjectMocks ExternalAPIService service;

  private final ExternalApiCallResponse exampleResponse =
      new ExternalApiCallResponse(200, "OK", null, 2);
  private static final String VALID_PARAM = "1-9";
  private static final String INVALID_PARAM = "ASD";

  @BeforeEach
  void setupValues() {
    ReflectionTestUtils.setField(service, "apiUrl", "url");
    ReflectionTestUtils.setField(service, "apiKey", "key");
    ReflectionTestUtils.setField(service, "cypherKey", "ionix123456");
  }

  @Test
  void externalApiCall_OKResponseTest() {
    ResponseEntity<ExternalApiCallResponse> responseEntity =
        new ResponseEntity<>(exampleResponse, HttpStatus.OK);
    Mockito.when(
            restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(ExternalApiCallResponse.class)))
        .thenReturn(responseEntity);
    ExternalApiCallResponse response =
        Assertions.assertDoesNotThrow(() -> service.getExternalApiResponse(VALID_PARAM));
    Assertions.assertEquals(response.getResult(), exampleResponse.getResult());
  }

  @Test
  void externalApiCall_wrongParameter_errorTest() {
    Mockito.when(
            restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(ExternalApiCallResponse.class)))
        .thenThrow(HttpClientErrorException.class);

    ResponseStatusException expectedException =
        Assertions.assertThrows(
            ResponseStatusException.class, () -> service.getExternalApiResponse(INVALID_PARAM));

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, expectedException.getStatus());
    Assertions.assertEquals(
        "there was an error processing your request. please double check",
        expectedException.getReason());
  }

  @Test
  void externalApiCall_serverError_test() {
    Mockito.when(
            restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(ExternalApiCallResponse.class)))
        .thenThrow(HttpServerErrorException.class);

    ResponseStatusException expectedException =
        Assertions.assertThrows(
            ResponseStatusException.class, () -> service.getExternalApiResponse(VALID_PARAM));

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, expectedException.getStatus());
    Assertions.assertEquals(
        "there was an error trying to fetch response from remote server",
        expectedException.getReason());
  }

  @Test
  void service_addsElapsedTime_assertion() {
    ResponseEntity<ExternalApiCallResponse> responseEntity =
        new ResponseEntity<>(exampleResponse, HttpStatus.OK);
    Mockito.when(
            restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(ExternalApiCallResponse.class)))
        .thenReturn(responseEntity);
    ExternalApiCallResponse response =
        Assertions.assertDoesNotThrow(() -> service.getExternalApiResponse(VALID_PARAM));
    Assertions.assertEquals(response.getResult(), exampleResponse.getResult());
    Assertions.assertNotNull(response.getElapsedTime());
  }
}
