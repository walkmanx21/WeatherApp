package org.walkmanx21.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.util.GetUserByCookieUtil;

import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final LocationService locationService;
    private final GetUserByCookieUtil getUserByCookieUtil;

    @Autowired
    public SearchController(LocationService locationService, GetUserByCookieUtil getUserByCookieUtil) {
        this.locationService = locationService;
        this.getUserByCookieUtil = getUserByCookieUtil;
    }

    @GetMapping
    public String searchWeather(@ModelAttribute("locationDto") FoundLocationDto locationDto, Model model, HttpServletRequest request) {
        Optional<User> mayBeUser = getUserByCookieUtil.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));

        FoundLocationDto[] foundLocations = locationService.findLocations(locationDto);
        model.addAttribute("locations", foundLocations);

        return "search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("location") FoundLocationDto foundLocationDto, HttpServletRequest request) {
        Optional<User> mayBeUser = getUserByCookieUtil.getUserByCookie(request);
        if (mayBeUser.isPresent()) {
            locationService.addLocation(foundLocationDto, mayBeUser.get());
            return "redirect/:index";
        } else {
            return "redirect/:sign-in";
        }

    }

}
