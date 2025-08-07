package org.walkmanx21.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;

@Controller("/")
public class MainPageController {

    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public MainPageController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> mayBeCookie = Arrays.stream(cookies).filter(c -> c.getName().equals("sessionId"))
                .findFirst();
        if (mayBeCookie.isPresent()) {
            UUID sessionId = UUID.fromString(mayBeCookie.get().getValue());
            Optional<Session> mayBeSession = sessionService.getCurrentSession(sessionId);
            if (mayBeSession.isPresent()) {
                Session session = mayBeSession.get();
                User user = userService.getUser(session.getUser().getId());
                model.addAttribute("user", user);
                return "index/index";
            }
        }
        return "index/index-without-user";
    }
}
