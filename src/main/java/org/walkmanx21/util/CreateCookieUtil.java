package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;

@Component
public class CreateCookieUtil {

    private static final int COOKIE_LIFETIME = 24 * 60 * 60;

    public Cookie createCookie (UserResponseDto userResponseDto) {
        Cookie cookie = new Cookie("sessionId", userResponseDto.getSessionId().toString());
        cookie.setMaxAge(COOKIE_LIFETIME);
        return cookie;
    }
}
