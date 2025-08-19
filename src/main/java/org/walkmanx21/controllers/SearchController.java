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

        Optional<User> mayBeUser = userService.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));

        if (mayBeUser.isEmpty()) {
            setRequestAttributes(foundLocationDto, request);
            return "redirect:/sign-in";
        }

        if (request.getSession().getAttribute("requestUrl") != null) {
            foundLocationDto = returnRequestFromSignIn(request);
        }

        List<FoundLocationDto> foundLocations = locationService.findLocations(foundLocationDto);
        model.addAttribute("locations", foundLocations);

        return "search-result/search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationDto") @Valid FoundLocationDto foundLocationDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (bindingResult.hasErrors())
            return "search-result/search-results-with-errors";

        Optional<User> mayBeUser = userService.getUserByCookie(request);
        mayBeUser.ifPresent(user -> model.addAttribute("user", user));

        if (mayBeUser.isPresent()) {
            try {
                locationService.addLocation(foundLocationDto, mayBeUser.get());
                return "redirect:/";
            } catch (LocationAlreadyExistException e) {
                bindingResult.rejectValue("name", "", e.getMessage());
                return "search-result/search-results-with-errors";
            }
        } else {
            return "redirect:/sign-in";
        }
    }

    private void setRequestAttributes(FoundLocationDto foundLocationDto, HttpServletRequest request) {
            request.getSession().setAttribute("requestUrl", request.getRequestURL());
            request.getSession().setAttribute("foundLocationDto", foundLocationDto);
    }

    private FoundLocationDto returnRequestFromSignIn(HttpServletRequest request) {
        FoundLocationDto foundLocationDto = (FoundLocationDto) request.getSession().getAttribute("foundLocationDto");
        request.getSession().removeAttribute("requestUrl");
        request.getSession().removeAttribute("foundLocationDto");
        return foundLocationDto;
    }

}
