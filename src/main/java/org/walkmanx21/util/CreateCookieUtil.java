package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;

@Component
public class CreateCookieUtil {

    public Cookie createCookie (UserResponseDto userResponseDto) {
        Cookie cookie = new Cookie("sessionId", userResponseDto.getSessionId().toString());
        cookie.setMaxAge(24*60*60);
        return cookie;
    }
}
