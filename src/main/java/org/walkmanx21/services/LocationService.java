package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.LocationDao;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationService {

    private final MappingUtil mappingUtil;
    private final LocationDao locationDao;
    private final OpenWeatherService openWeatherService;

    @Autowired
    public LocationService(MappingUtil mappingUtil, LocationDao locationDao, OpenWeatherService openWeatherService) {
        this.mappingUtil = mappingUtil;
        this.locationDao = locationDao;
        this.openWeatherService = openWeatherService;
    }


    public List<FoundLocationDto> findLocations(FoundLocationDto foundLocationDto) {
        return openWeatherService.findLocations(foundLocationDto);
    }

    public void addLocation(FoundLocationDto foundLocationDto, User user) {
        Location location = mappingUtil.convertToLocation(foundLocationDto);
        location.setUser(user);
        locationDao.insertLocation(location);
    }

    public List<WeatherResponseDto> getWeatherDataForAllLocations(User user) {
        List<Location> locations = locationDao.getAllUserLocations(user);
        return locations.stream().map(this::getWeatherData).collect(Collectors.toList());
    }

    public WeatherResponseDto getWeatherData(Location location) {
        return openWeatherService.getWeatherData(location);
    }


    public void deleteLocation(WeatherResponseDto weatherResponseDto, User user) {
        locationDao.deleteLocation(weatherResponseDto, user);
    }
}
