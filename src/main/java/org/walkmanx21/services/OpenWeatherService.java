package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.OpenWeatherResponseDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.exceptions.BadRequestForWeatherApiServiceException;
import org.walkmanx21.exceptions.WeatherApiServiceUnavailableException;
import org.walkmanx21.models.Location;
import org.walkmanx21.util.HttpClientUtil;
import org.walkmanx21.util.MappingUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class OpenWeatherService {

    private final MappingUtil mappingUtil;
    @Value("${api.key}")
    private String apiKey;

    private static final int COUNT_OF_LOCATIONS_LIMIT = 5;

    private final HttpClientUtil httpClient;

    @Autowired
    public OpenWeatherService(HttpClientUtil httpClient, MappingUtil mappingUtil) {
        this.httpClient = httpClient;
        this.mappingUtil = mappingUtil;
    }

    public List<FoundLocationDto> findLocations (FoundLocationDto foundLocationDto) {
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + COUNT_OF_LOCATIONS_LIMIT + "&appid=" + apiKey;

        try {
            ResponseEntity<FoundLocationDto[]> response = httpClient.getData(url, FoundLocationDto[].class);
            if (response != null)
                return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException e) {
            throwNewCustomExceptions(e);
        }

        return null;
    }

    public WeatherResponseDto getWeatherData(Location location) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey + "&units=metric";

        try {
            ResponseEntity<OpenWeatherResponseDto> response = httpClient.getData(url, OpenWeatherResponseDto.class);
            OpenWeatherResponseDto openWeatherResponseDto = response.getBody();
            if (openWeatherResponseDto != null) {
                openWeatherResponseDto.setId(location.getId());
                return buildWeatherResponseDto(openWeatherResponseDto);
            }
        } catch (HttpClientErrorException e) {
            throwNewCustomExceptions(e);
        }

        return null;
    }

    private WeatherResponseDto buildWeatherResponseDto(OpenWeatherResponseDto openWeatherResponseDto) {
        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();

        weatherResponseDto.setId(openWeatherResponseDto.getId());
        weatherResponseDto.setName(openWeatherResponseDto.getName());
        weatherResponseDto.setCountry(openWeatherResponseDto.getSys().get("country"));
        weatherResponseDto.setLatitude(openWeatherResponseDto.getCoord().get("lat"));
        weatherResponseDto.setLongitude(openWeatherResponseDto.getCoord().get("lon"));
        weatherResponseDto.setTemperature((int) Math.round(openWeatherResponseDto.getMain().get("temp")));
        weatherResponseDto.setFeelsLike((int) Math.round(openWeatherResponseDto.getMain().get("feels_like")));
        weatherResponseDto.setDescription(capitalizeFirstLetter(openWeatherResponseDto.getWeather()[0].get("description")));
        weatherResponseDto.setHumidity(openWeatherResponseDto.getMain().get("humidity").toString());
        weatherResponseDto.setWeatherIcon(openWeatherResponseDto.getWeather()[0].get("icon"));

        return weatherResponseDto;
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

    private void throwNewCustomExceptions(HttpClientErrorException e) {
        int errorCode = e.getStatusCode().value();
        if (errorCode >= 400 && errorCode < 500) {
            throw new BadRequestForWeatherApiServiceException("An uncorrected request. Check the request parameters.");
        } else {
            throw new WeatherApiServiceUnavailableException("The weather data service is unavailable.");
        }
    }
}
