package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.models.User;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.util.CookieUtil;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final LocationService locationService;
    private final CookieUtil cookieUtil;

    @Autowired
    public SearchController(LocationService locationService, CookieUtil cookieUtil) {
        this.locationService = locationService;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping
    public String searchLocation(@ModelAttribute("locationDto") FoundLocationDto foundLocationDto, Model model, HttpServletRequest request) {
        Optional<User> mayBeUser = cookieUtil.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));

        List<FoundLocationDto> foundLocations = locationService.findLocations(foundLocationDto);
        model.addAttribute("locations", foundLocations);

        return "search-result/search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationDto") FoundLocationDto foundLocationDto, HttpServletRequest request) {

        Optional<User> mayBeUser = cookieUtil.getUserByCookie(request);
        if (mayBeUser.isPresent()) {
            locationService.addLocation(foundLocationDto, mayBeUser.get());
            return "redirect:/";
        }
        return "redirect:/sign-in";
    }

}
