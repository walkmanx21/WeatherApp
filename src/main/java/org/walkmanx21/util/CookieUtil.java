package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CookieUtil {

    @Value("${lifetime.duration}")
    private int cookieLifetime;

    public void setSessionId(String sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setMaxAge(cookieLifetime);
        response.addCookie(cookie);
    }

    public void deleteSessionId(HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
