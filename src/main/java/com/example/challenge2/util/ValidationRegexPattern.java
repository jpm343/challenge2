package com.example.challenge2.util;

public class ValidationRegexPattern {

  public static final String USERNAME = "^[A-Za-z0-9._@\\-\\s\\u00C0-\\u00FF]+$";

  public static final String PHONE_NUMBER = "^(\\+(?:[0-9] ?){6,14}[0-9])|$";

  public static final String NON_ALPHANUMERIC_NAME = "^[A-Za-z0-9\\s\\u00C0-\\u00FF]*$";
}
