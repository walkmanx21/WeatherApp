package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.LocationDao;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.OpenWeatherResponseDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.util.HttpClientUtil;
import org.walkmanx21.util.MappingUtil;

@Component
public class LocationService {


    @Value("${api.key}")
    private String apiKey;

    private static final int COUNT_OF_LOCATIONS_LIMIT = 5;

    private final MappingUtil mappingUtil;
    private final LocationDao locationDao;
    private final HttpClientUtil httpClient;

    @Autowired
    public LocationService(MappingUtil mappingUtil, LocationDao locationDao, HttpClientUtil httpClient) {
        this.mappingUtil = mappingUtil;
        this.locationDao = locationDao;
        this.httpClient = httpClient;
    }


    public FoundLocationDto[] findLocations (FoundLocationDto foundLocationDto) {
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + COUNT_OF_LOCATIONS_LIMIT + "&appid=" + apiKey;
        ResponseEntity<FoundLocationDto[]> response = httpClient.getData(url, FoundLocationDto[].class);
        return response.getBody();
    }

    public void addLocation(FoundLocationDto foundLocationDto, User user) {
        Location location = mappingUtil.convertToLocation(foundLocationDto);
        location.setUser(user);
        locationDao.insertLocation(location);
    }

    public WeatherResponseDto getWeatherData(Location location) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey + "&units=metric";
        ResponseEntity<OpenWeatherResponseDto> response = httpClient.getData(url, OpenWeatherResponseDto.class);
        OpenWeatherResponseDto openWeatherResponseDto = response.getBody();
        if (openWeatherResponseDto != null) {
            return buildWeatherResponseDto(openWeatherResponseDto);
        }
        return null;
    }

    private WeatherResponseDto buildWeatherResponseDto(OpenWeatherResponseDto openWeatherResponseDto) {

        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();

        //Присваиваем имя
        weatherResponseDto.setName(openWeatherResponseDto.getName());
        //Присваиваем страну
        weatherResponseDto.setCountry(openWeatherResponseDto.getSys().get("country"));
        //Присваиваем температуру
        long temperature = Math.round(openWeatherResponseDto.getMain().get("temp"));
        weatherResponseDto.setTemperature(temperature + "°C");
        //Присваиваем "ощущается как"
        long feelsLike = Math.round(openWeatherResponseDto.getMain().get("feels_like"));
        weatherResponseDto.setFeelsLike(feelsLike + "°C");
        //Присваиваем description
        weatherResponseDto.setDescription(capitalizeFirstLetter(openWeatherResponseDto.getWeather()[0].get("description")));
        //Присваиваем влажность
        weatherResponseDto.setHumidity(openWeatherResponseDto.getMain().get("humidity").toString() + "%");

        return weatherResponseDto;
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

}
