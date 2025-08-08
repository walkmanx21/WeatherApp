package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;

@Component
public class SetCookieUtil {

    @Value("${lifetime.duration}")
    private int cookieLifetime;

    public Cookie setSessionId(UserResponseDto userResponseDto) {
        Cookie cookie = new Cookie("sessionId", userResponseDto.getSessionId().toString());
        cookie.setMaxAge(cookieLifetime);
        return cookie;
    }
}
