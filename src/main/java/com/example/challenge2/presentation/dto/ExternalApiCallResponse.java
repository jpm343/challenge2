package com.example.challenge2.presentation.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExternalApiCallResponse {
  int responseCode;
  String description;
  Long elapsedTime;
  Result result;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  private static class Result {
    int registerCount;
  }

  public ExternalApiCallResponse(int responseCode, String description, Long elapsedTime, int registerCount) {
    this.responseCode = responseCode;
    this.description = description;
    this.elapsedTime = elapsedTime;
    this.result = new Result(registerCount);
  }
}
