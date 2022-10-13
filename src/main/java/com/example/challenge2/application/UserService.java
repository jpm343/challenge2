package com.example.challenge2.application;

import com.example.challenge2.domain.model.User;
import com.example.challenge2.domain.repository.UserRepository;
import com.example.challenge2.presentation.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserDTO> getAll() {
    LOGGER.info("getAll|in.");
    List<UserDTO> response =
        userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    LOGGER.info("getAll|out. totalUsers={}", response.size());
    return response;
  }

  public UserDTO getByEmail(String email) {
    LOGGER.info("getByEmail|in. email={}", email);
    User response = findUserByEmailOrThrowException(email);
    LOGGER.info("getByEmail|out. found with id={}", response.getId());
    return this.mapToDTO(response);
  }

  public User create(UserDTO userDTO) {
    if (null == userDTO) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user.userDto.notNull");
    }
    LOGGER.info("create|in. username={}", userDTO.getUsername());
    final var newUser = prepareUser(userDTO);
    User createdUser = userRepository.save(newUser);
    LOGGER.info("create|out. user created with id={}", createdUser.getId());
    return createdUser;
  }

  public void deleteByUserName(String userName) {
    LOGGER.info("deleteByUserName|in. username={}", userName);
    final var user = findUserByUsernameOrThrowException(userName);
    userRepository.delete(user);
    LOGGER.info("deleteByUserName|out.");
  }

  private UserDTO mapToDTO(User user) {
    return new UserDTO(user.getName(), user.getUsername(), user.getEmail(), user.getPhone());
  }

  private User prepareUser(UserDTO userDTO) {
    return new User(
        userDTO.getName(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPhone());
  }

  private User findUserByUsernameOrThrowException(String username) {
    return userRepository
        .findUserByUsername(username)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user.userRecord.notFound"));
  }

  private User findUserByEmailOrThrowException(String email) {
    return userRepository
        .findUserByEmail(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user.userRecord.notFound"));
  }
}
