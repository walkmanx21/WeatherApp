package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class CookieUtil {

    @Value("${lifetime.duration}")
    private int cookieLifetime;

    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public CookieUtil(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

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

    public Optional<User> getUserByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> mayBeCookie = Optional.empty();
        if (cookies != null) {
            mayBeCookie = Arrays.stream(cookies).filter(c -> c.getName().equals("sessionId"))
                    .findFirst();
        }

        if (mayBeCookie.isPresent()) {
            UUID sessionId = UUID.fromString(mayBeCookie.get().getValue());
            Optional<Session> mayBeSession = sessionService.getCurrentSession(sessionId);
            if (mayBeSession.isPresent()) {
                Session session = mayBeSession.get();
                return Optional.of(userService.getUser(session.getUser().getId()));
            }
        }
        return Optional.empty();
    }
}
