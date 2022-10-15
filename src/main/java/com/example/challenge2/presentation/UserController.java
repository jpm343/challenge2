package com.example.challenge2.presentation;

import com.example.challenge2.application.UserService;
import com.example.challenge2.domain.model.User;
import com.example.challenge2.presentation.dto.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.text.StringEscapeUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(final UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "gets all users")
  public List<UserDTO> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("/{email}")
  @Operation(summary = "gets an user by email")
  public UserDTO getUserByEmail(@PathVariable String email) {
    String sanitizedEmail = StringEscapeUtils.escapeJava(email);
    return userService.getByEmail(sanitizedEmail);
  }

  @PostMapping
  @Operation(
      security = {
        @SecurityRequirement(name = "BasicAuthentication"),
        @SecurityRequirement(name = "user"),
        @SecurityRequirement(name = "pass")
      },
      summary = "creates an user")
  public User createUser(@RequestBody @Valid UserDTO userDTO) {
    return userService.create(userDTO);
  }

  @DeleteMapping("/{username}")
  @Operation(
      security = {
        @SecurityRequirement(name = "BasicAuthentication"),
        @SecurityRequirement(name = "user"),
        @SecurityRequirement(name = "pass")
      },
      summary = "deletes an user given its username")
  public void deleteUser(@PathVariable String username) {
    String sanitizedUsername = StringEscapeUtils.escapeJava(username);
    userService.deleteByUserName(sanitizedUsername);
  }
}
