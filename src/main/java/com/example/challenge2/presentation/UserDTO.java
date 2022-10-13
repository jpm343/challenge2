package com.example.challenge2.presentation;

import com.example.challenge2.util.ValidationRegexPattern;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class UserDTO {
    @NotNull(message = "{userAccount.name.notNull}")
    @Length(max = 256, message = "{userAccount.name.maxLength}")
    @Pattern(
            regexp = ValidationRegexPattern.NON_ALPHANUMERIC_NAME,
            message = "{userAccount.name.pattern}")
    String name;

    @NotNull(message = "{userAccount.username.notNull}")
    @Length(max = 256, message = "{userAccount.username.maxLength}")
    @Pattern(regexp = ValidationRegexPattern.USERNAME, message = "{userAccount.username.pattern}")
    String username;

    @NotNull(message = "{userAccount.email.notNull}")
    @Length(max = 256, message = "{userAccount.email.maxLength}")
    @Email(message = "{userAccount.email.isNotValid}")
    String email;

    @NotNull(message = "{userAccount.phone.notNull}")
    @Length(max = 256, message = "{userAccount.phone.maxLength}")
    @Pattern(regexp = ValidationRegexPattern.PHONE_NUMBER, message = "{userAccount.phone.pattern}")
    String phone;
}
