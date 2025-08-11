package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;

@Component
public class SetCookieUtil {

    @Value("${lifetime.duration}")
    private int cookieLifetime;

    public void setSessionId(UserResponseDto userResponseDto, HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", userResponseDto.getSessionId().toString());
        cookie.setMaxAge(cookieLifetime);
        response.addCookie(cookie);
    }

    public void deleteSessionId(HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
