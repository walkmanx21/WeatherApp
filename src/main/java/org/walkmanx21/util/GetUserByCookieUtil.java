package org.walkmanx21.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetUserByCookieUtil {

    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public GetUserByCookieUtil(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
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
