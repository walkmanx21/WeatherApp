package org.walkmanx21.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters long")
    private String login;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String repeatPassword;
}
