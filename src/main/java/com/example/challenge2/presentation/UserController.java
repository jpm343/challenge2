package com.example.challenge2.presentation;

import com.example.challenge2.application.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  public UserController(final UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "get all users")
  public List<UserDTO> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("")

}
