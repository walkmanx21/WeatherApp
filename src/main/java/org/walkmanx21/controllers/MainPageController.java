package org.walkmanx21.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.models.User;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.util.GetUserByCookieUtil;
import org.walkmanx21.util.SetCookieUtil;

import java.util.List;
import java.util.Optional;

@Controller("/")
public class MainPageController {

    private final GetUserByCookieUtil getUserByCookieUtil;
    private final SetCookieUtil setCookieUtil;
    private final LocationService locationService;

    @Autowired
    public MainPageController(GetUserByCookieUtil getUserByCookieUtil, SetCookieUtil setCookieUtil, LocationService locationService) {
        this.getUserByCookieUtil = getUserByCookieUtil;
        this.setCookieUtil = setCookieUtil;
        this.locationService = locationService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        Optional<User> mayBeUser = getUserByCookieUtil.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));
        List<WeatherResponseDto> weatherResponseDtoList = null;

        if (mayBeUser.isPresent()) {
            weatherResponseDtoList = locationService.getWeatherDataForAllLocations(mayBeUser.get());
            model.addAttribute("weatherResponseDtoList", weatherResponseDtoList);
        }
        return "index";
    }

    @PostMapping
    public String signOut (HttpServletRequest request) {
        setCookieUtil.deleteSessionId(request);
        return "index";
    }

}
