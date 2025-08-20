package org.walkmanx21.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.exceptions.LocationAlreadyExistException;
import org.walkmanx21.models.User;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.services.UserService;
import org.walkmanx21.util.CookieUtil;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final LocationService locationService;
    private final UserService userService;

    @Autowired
    public SearchController(LocationService locationService, CookieUtil cookieUtil, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @GetMapping
    public String searchLocation(@ModelAttribute("locationDto") FoundLocationDto foundLocationDto, Model model, HttpServletRequest request) {
        return userService.getUserByCookie(request).map(user -> {
            model.addAttribute("user", user);
            model.addAttribute("locations", locationService.findLocations(foundLocationDto));
            return "search-result/search-results";
        }).orElse("redirect:/sign-in");
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationDto") @Valid FoundLocationDto foundLocationDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (bindingResult.hasErrors())
            return "search-result/search-results-with-errors";

        return userService.getUserByCookie(request).map(user -> {
            try {
                model.addAttribute("user", user);
                locationService.addLocation(foundLocationDto, user);
                return "redirect:/";
            } catch (LocationAlreadyExistException e) {
                bindingResult.rejectValue("name", "", e.getMessage());
                return "search-result/search-results-with-errors";
            }
        }).orElse("redirect:/sign-in");
    }

}
