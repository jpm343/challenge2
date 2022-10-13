package com.example.challenge2.domain.model;

import com.example.challenge2.util.ValidationRegexPattern;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(
    name = "user_account",
    indexes = {@Index(name = "IDX_USRACCT_USERNAME", columnList = "username")})
public class User {

  private static final long serialVersionUID = 197827316467080064L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, length = 256)
  @NotNull(message = "{userAccount.name.notNull}")
  @Pattern(
      regexp = ValidationRegexPattern.NON_ALPHANUMERIC_NAME,
      message = "{userAccount.name.pattern}")
  private String name;

  @Column(unique = true, nullable = false, length = 256)
  @NotNull(message = "{userAccount.username.notNull}")
  @Pattern(regexp = ValidationRegexPattern.USERNAME, message = "{userAccount.username.pattern}")
  private String username;

  @Column(unique = true, nullable = false, length = 256)
  @NotNull(message = "{userAccount.email.notNull}")
  @Email(message = "{userAccount.email.isNotValid}")
  private String email;

  @Column(unique = true, nullable = false, length = 256)
  @NotNull(message = "{userAccount.phone.notNull}")
  @Pattern(regexp = ValidationRegexPattern.PHONE_NUMBER, message = "{userAccount.phone.pattern}")
  private String phone;

  public User(String name, String username, String email, String phone) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.phone = phone;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
  }
}
