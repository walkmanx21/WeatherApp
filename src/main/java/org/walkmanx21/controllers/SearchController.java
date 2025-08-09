package org.walkmanx21.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.services.LocationService;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final LocationService locationService;

    @Autowired
    public SearchController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public String searchWeather(@ModelAttribute("locationDto") FoundLocationDto locationDto, Model model) {
        FoundLocationDto[] foundLocations = locationService.findLocations(locationDto);
        model.addAttribute("locations", foundLocations);
        return "search-results";
    }

}
