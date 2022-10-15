package com.example.challenge2.application;

import com.example.challenge2.domain.model.User;
import com.example.challenge2.domain.repository.UserRepository;
import com.example.challenge2.presentation.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository repository;

  @InjectMocks private UserService service;

  private static final User user1 = new User("name", "username", "mail@mail.cl", "+569123456");
  private static final User user2 = new User("name2", "username2", "mail2@mail.cl", "+569123456");

  @Test
  void getAllUsers_OKTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of(user1, user2));
    List<UserDTO> expectedMappedElements = Stream.of(user1, user2).map(this::mapToDto).toList();
    List<UserDTO> response = Assertions.assertDoesNotThrow(() -> service.getAll());
    Assertions.assertEquals(response.size(), expectedMappedElements.size());
  }

  @Test
  void getAllUsers_emptyResponse_OKTest() {
    Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
    List<UserDTO> response = Assertions.assertDoesNotThrow(() -> service.getAll());
    Assertions.assertTrue(response.isEmpty());
  }

  @Test
  void getUserByEmail_withExistentEmail_OKTest() {
    String existentEmail = "mail@mail.cl";
    Mockito.when(repository.findUserByEmail(existentEmail)).thenReturn(Optional.of(user1));
    UserDTO response = Assertions.assertDoesNotThrow(() -> service.getByEmail(existentEmail));
    Assertions.assertEquals(response.getUsername(), user1.getUsername());
  }

  @Test
  void getUserByEmail_withNonExistentEmail_thenThrowNotFound() {
    String nonExistentEmail = "fake@fake.cl";
    Mockito.when(repository.findUserByEmail(nonExistentEmail)).thenReturn(Optional.empty());
    ResponseStatusException expectedException =
        Assertions.assertThrows(
            ResponseStatusException.class, () -> service.getByEmail(nonExistentEmail));
    Assertions.assertEquals(HttpStatus.NOT_FOUND, expectedException.getStatus());
  }

  @Test
  void createUser_okTest() {
    Mockito.when(repository.findUserByUsername(user1.getUsername())).thenReturn(Optional.empty());
    Mockito.when(repository.findUserByEmail(user1.getEmail())).thenReturn(Optional.empty());
    Mockito.when(repository.save(any())).thenReturn(user1);

    User createUserResponse = Assertions.assertDoesNotThrow(() -> service.create(mapToDto(user1)));
    Assertions.assertEquals(createUserResponse.getUsername(), user1.getUsername());
  }

  @Test
  void createUser_withAlreadyRegisteredMail_thenThrowConflict() {
    String alreadyRegisteredEmail = "already@registered.cl";
    user1.setEmail(alreadyRegisteredEmail);
    Mockito.when(repository.findUserByUsername(user1.getUsername())).thenReturn(Optional.empty());
    Mockito.when(repository.findUserByEmail(alreadyRegisteredEmail)).thenReturn(Optional.of(user1));

    ResponseStatusException expectedException =
        Assertions.assertThrows(
            ResponseStatusException.class, () -> service.create(mapToDto(user1)));
    Assertions.assertEquals(HttpStatus.CONFLICT, expectedException.getStatus());
  }

  @Test
  void createUser_withAlreadyRegisteredUsername_thenThrowConflict() {
    String alreadyRegisteredUsername = "alreadyRegistered";
    user1.setUsername(alreadyRegisteredUsername);
    Mockito.when(repository.findUserByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

    ResponseStatusException expectedException =
        Assertions.assertThrows(
            ResponseStatusException.class, () -> service.create(mapToDto(user1)));
    Assertions.assertEquals(HttpStatus.CONFLICT, expectedException.getStatus());
  }

  private UserDTO mapToDto(User user) {
    return new UserDTO(user.getName(), user.getUsername(), user.getEmail(), user.getPhone());
  }
}
