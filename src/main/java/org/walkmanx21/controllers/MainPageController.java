package org.walkmanx21.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.models.Session;
import org.walkmanx21.services.SessionService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Controller("/")
public class MainPageController {

    private final SessionService sessionService;

    @Autowired
    public MainPageController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String index(@ModelAttribute("userRequestDto") UserRequestDto userRequestDto, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> mayBeCookie = Arrays.stream(cookies).filter(c -> c.getName().equals("sessionId"))
                .findFirst();
        if(mayBeCookie.isPresent()) {
            UUID sessionId = UUID.fromString(mayBeCookie.get().getValue());
            int userId = (int) httpSession.getAttribute("userId");
            Optional<Session> mayBeSession = sessionService.getCurrentSession(userId);
            if (mayBeSession.isPresent()) {
                Session session = mayBeSession.get();
                if (session.getId().equals(sessionId)) {
                    return "index/index";
                }
            }
        }
        return "index/index-without-user";
    }
}
