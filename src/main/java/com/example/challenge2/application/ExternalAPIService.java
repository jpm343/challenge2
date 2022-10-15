package com.example.challenge2.application;

import com.example.challenge2.presentation.dto.ExternalApiCallResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

@Service
public class ExternalAPIService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExternalAPIService.class);

  private final RestTemplate externalAPIRestTemplate;
  private static final String API_KEY_HEADER_NAME = "X-Api-Key";

  public ExternalAPIService(RestTemplate externalAPIRestTemplate) {
    this.externalAPIRestTemplate = externalAPIRestTemplate;
  }

  @Value("${api.external.url}")
  private String apiUrl;

  @Value("${api.external.key}")
  private String apiKey;

  @Value("${api.external.cypher}")
  private String cypherKey;

  public ExternalApiCallResponse getExternalApiResponse(String param) {
    LOGGER.info("getExternalApiResponse|in. param={}", param);
    String sanitizedParam = StringEscapeUtils.escapeJava(param);
    String cypheredParam;
    try {
      cypheredParam = cypherParam(sanitizedParam);
    } catch (Exception e) {
      LOGGER.error(
          "there was an error trying to encrypt param={}. trace={}", param, e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error decrypting param");
    }

    try {
      HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders());
      long start = System.currentTimeMillis();
      ResponseEntity<ExternalApiCallResponse> response =
          externalAPIRestTemplate.exchange(
              apiUrl + cypheredParam, HttpMethod.GET, requestEntity, ExternalApiCallResponse.class);
      long finish = System.currentTimeMillis();
      long timeElapsed = finish - start;

      Objects.requireNonNull(response.getBody()).setElapsedTime(timeElapsed);

      return response.getBody();
    } catch (HttpServerErrorException e) {
      LOGGER.error(e.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "there was an error trying to fetch response from remote server");
    } catch (HttpClientErrorException e) {
      LOGGER.error(e.getMessage());
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "there was an error processing your request. please double check");
    }
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(API_KEY_HEADER_NAME, apiKey);
    return headers;
  }

  private String cypherParam(String param)
      throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
          NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
          InvalidKeySpecException {
    DESKeySpec keySpec = new DESKeySpec(cypherKey.getBytes("UTF8"));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey key = keyFactory.generateSecret(keySpec);
    byte[] cleartext = param.getBytes("UTF8");
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return Base64.getEncoder().encodeToString(cipher.doFinal(cleartext));
  }
}
