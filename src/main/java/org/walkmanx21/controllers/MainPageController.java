package org.walkmanx21.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;

@Controller("/")
public class MainPageController {

    private final LocationService locationService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public MainPageController(LocationService locationService, SessionService sessionService, UserService userService) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        return userService.getUserByCookie(request).map(user -> {
            model.addAttribute("user", user);
            model.addAttribute("weatherResponseDtoList", locationService.getWeatherDataForAllLocations(user));
            return "index";
        }).orElse("redirect:/sign-in");
    }

    @PostMapping
    public String signOut (HttpServletResponse response) {
        sessionService.deleteSessionId(response);
        return "redirect:/";
    }

    @DeleteMapping
    public String deleteLocation(@ModelAttribute ("weatherResponseDto") WeatherResponseDto weatherResponseDto, HttpServletRequest request) {
        userService.getUserByCookie(request).ifPresent(user -> locationService.deleteLocation(weatherResponseDto, user));
        return "redirect:/";

    }

}
