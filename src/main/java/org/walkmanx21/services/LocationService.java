package org.walkmanx21.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.walkmanx21.dto.OpenWeatherResponseDto;
import org.walkmanx21.models.Location;

import java.util.Arrays;
import java.util.Optional;

@Component
public class LocationService {

    private static final String API_KEY = "5030597daf2fce3ea252d67eaf12b843";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();


    public void getWeatherForecast(Location location) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ location.getName() + "&appid=" + API_KEY +"&units=metric";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<OpenWeatherResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, OpenWeatherResponseDto.class);

        OpenWeatherResponseDto responseDto = response.getBody();

        Optional<String> mayBeMain = Arrays.stream(responseDto.getWeather()).map(e -> e.get("main")).findFirst();
        String main  = null;
        if (mayBeMain.isPresent()) {
            main = mayBeMain.get();
        }

        System.out.println(main);
    }
}
