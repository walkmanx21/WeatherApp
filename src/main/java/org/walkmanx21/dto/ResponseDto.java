package org.walkmanx21.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private int userId;
    private String login;
    private UUID sessionId;
    private boolean error;
    private String errorField;
    private String errorMessage;

    public ResponseDto(boolean error, String errorField, String errorMessage) {
        this.error = error;
        this.errorField = errorField;
        this.errorMessage = errorMessage;
    }
}

