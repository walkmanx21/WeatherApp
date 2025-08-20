package org.walkmanx21.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private int userId;
    private String login;
    private UUID sessionId;
    private boolean error;
    private String errorField;
    private String errorMessage;
}

