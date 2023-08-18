package com.onlinebookstore.dto.user;

import com.onlinebookstore.validation.Email;
import com.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Password and repeat password shouldn't be empty and should be equal"
)
public class UserRegistrationRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50)
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
