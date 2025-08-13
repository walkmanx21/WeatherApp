package org.walkmanx21.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.walkmanx21.config.DataSourceConfig;
import org.walkmanx21.config.FlywayConfig;
import org.walkmanx21.config.PropertyConfig;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.OpenWeatherResponseDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.exceptions.BadRequestForWeatherApiServiceException;
import org.walkmanx21.exceptions.WeatherApiServiceUnavailableException;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.services.OpenWeatherService;
import org.walkmanx21.util.HttpClientUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class OpenWeatherServiceIT {

    @Value("${api.key}")
    private String apiKey;
    private static final int COUNT_OF_LOCATIONS_LIMIT = 5;

    private FoundLocationDto[] foundLocationDtos;
    private OpenWeatherResponseDto openWeatherResponseDto;
    private FoundLocationDto foundLocationDto;
    private Location location;


    @Autowired
    private OpenWeatherService openWeatherService;

    @MockitoBean
    private HttpClientUtil httpClient;

    @BeforeEach
    void prepare() {
        location = new Location(237, "Moscow", new User(), new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "RU", "");
        foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName("Moscow");
        foundLocationDtos = new FoundLocationDto[] {
                new FoundLocationDto("Moscow", new BigDecimal("55.22231131"), new BigDecimal("37.12355151"), "RU", "Moscow obl"),
                new FoundLocationDto("Moscow", new BigDecimal("45.22231131"), new BigDecimal("47.12355151"), "US", "Texas"),
                new FoundLocationDto("Moscow", new BigDecimal("35.22212331"), new BigDecimal("27.12355151"), "FR", "Universe"),
                new FoundLocationDto("Moscow", new BigDecimal("25.22123321"), new BigDecimal("17.12355151"), "GD", "Something"),
                new FoundLocationDto("Moscow", new BigDecimal("15.21231231"), new BigDecimal("57.12123551"), "FT", "Mars"),
                new FoundLocationDto("Moscow", new BigDecimal("25.22123321"), new BigDecimal("17.12355151"), "GD", "Something")
        };

        int id = 237;
        String name = "Moscow";
        Map<String, String> sys = Map.of(
                "type", "2",
                "id", "2094500",
                "country", "RU",
                "sunrise", "1755050255",
                "sunset", "1755105085");
        Map<String, BigDecimal> coord = Map.of(
                "lon", new BigDecimal("37.6187"),
                "lat", new BigDecimal("55.7487")
        );
        Map<String, Double> main = Map.of(
                "temp", 22.95,
                "feels_like", 22.68,
                "temp_min", 21.29,
                "temp_max", 23.01,
                "pressure", 1017.00,
                "humidity", 57.00,
                "sea_level", 1017.00,
                "grnd_level", 998.00
        );
        Map<String, String>[] weather = new Map[]{Map.of(
                "id", "800",
                "main", "Clear",
                "description", "clear sky",
                "icon", "01d"
        )};

        openWeatherResponseDto = new OpenWeatherResponseDto(id, name, sys, coord, main, weather);
    }

    @Test
    void whenFindLocationByNameReturnListOfLocations(){
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + COUNT_OF_LOCATIONS_LIMIT + "&appid=" + apiKey;

        ResponseEntity<FoundLocationDto[]> response = new ResponseEntity<>(foundLocationDtos, HttpStatus.OK);
        Mockito.doReturn(response).when(httpClient).getData(url, FoundLocationDto[].class);
        List<FoundLocationDto> locations = openWeatherService.findLocations(foundLocationDto);

        Assertions.assertEquals(6, locations.size());
    }

    @Test
    void whenRequestForWeatherDataReturnWeatherResponseDto() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey + "&units=metric";

        ResponseEntity<OpenWeatherResponseDto> response = new ResponseEntity<>(openWeatherResponseDto, HttpStatus.OK);
        Mockito.doReturn(response).when(httpClient).getData(url, OpenWeatherResponseDto.class);
        WeatherResponseDto weatherResponseDto = openWeatherService.getWeatherData(location);

        Assertions.assertNotNull(weatherResponseDto);
        Assertions.assertEquals(location.getId(), weatherResponseDto.getId());
        Assertions.assertEquals(location.getName(), weatherResponseDto.getName());
        Assertions.assertEquals(openWeatherResponseDto.getSys().get("country"), weatherResponseDto.getCountry());
    }

    @Test
    void whenResponseHttpStatus400ThenThrowNewBadRequestForWeatherApiServiceException() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey + "&units=metric";

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(httpClient).getData(url, OpenWeatherResponseDto.class);

        Assertions.assertThrows(BadRequestForWeatherApiServiceException.class, () -> openWeatherService.getWeatherData(location));

    }

    @Test
    void whenResponseHttpStatus500ThenThrowNewWeatherApiServiceUnavailableException() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + apiKey + "&units=metric";

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(httpClient).getData(url, OpenWeatherResponseDto.class);

        Assertions.assertThrows(WeatherApiServiceUnavailableException.class, () -> openWeatherService.getWeatherData(location));
    }
}
