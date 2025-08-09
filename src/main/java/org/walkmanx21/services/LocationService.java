package org.walkmanx21.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.OpenWeatherResponseDto;
import org.walkmanx21.models.Location;

@Component
public class LocationService {

    @Value("${api.key}")
    private String apiKey;

    private int limit = 5;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();


    public void getWeatherForecast(Location location) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ location.getName() + "&appid=" + apiKey +"&units=metric";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<OpenWeatherResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, OpenWeatherResponseDto.class);

        OpenWeatherResponseDto responseDto = response.getBody();

        System.out.println();
    }

    public FoundLocationDto[] findLocations (FoundLocationDto foundLocationDto) {
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + limit + "&appid=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<FoundLocationDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, FoundLocationDto[].class);

        return response.getBody();
    }
}
