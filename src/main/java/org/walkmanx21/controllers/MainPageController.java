package org.walkmanx21.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.walkmanx21.models.User;
import org.walkmanx21.util.GetUserByCookieUtil;
import org.walkmanx21.util.SetCookieUtil;

import java.util.Optional;

@Controller("/")
public class MainPageController {

    private final GetUserByCookieUtil getUserByCookieUtil;
    private final SetCookieUtil setCookieUtil;

    @Autowired
    public MainPageController(GetUserByCookieUtil getUserByCookieUtil, SetCookieUtil setCookieUtil) {
        this.getUserByCookieUtil = getUserByCookieUtil;
        this.setCookieUtil = setCookieUtil;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        Optional<User> mayBeUser = getUserByCookieUtil.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));
        return "index";
    }

    @PostMapping
    public String signOut (HttpServletRequest request) {
        setCookieUtil.deleteSessionId(request);
        return "index";
    }

}
