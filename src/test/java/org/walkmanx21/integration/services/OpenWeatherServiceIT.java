package org.walkmanx21.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.walkmanx21.config.DataSourceConfig;
import org.walkmanx21.config.FlywayConfig;
import org.walkmanx21.config.PropertyConfig;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.services.OpenWeatherService;
import org.walkmanx21.util.HttpClientUtil;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class OpenWeatherServiceIT {

    @Value("${api.key}")
    private String apiKey;

    private static final int COUNT_OF_LOCATIONS_LIMIT = 5;

    private final FoundLocationDto foundLocationDto = new FoundLocationDto();

    @Autowired
    private OpenWeatherService openWeatherService;

    @MockitoBean
    private HttpClientUtil httpClient;

    private final Location location = new Location(237, "Moscow", new User(), new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "RU", "");


    @BeforeEach
    void prepare() {
        foundLocationDto.setName("Moscow");
    }

    @Test
    void test(){
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + COUNT_OF_LOCATIONS_LIMIT + "&appid=" + apiKey;

        FoundLocationDto[] body = new FoundLocationDto[] {
                new FoundLocationDto("Moscow", new BigDecimal("55.22231131"), new BigDecimal("37.12355151"), "RU", "Moscow obl"),
                new FoundLocationDto("Moscow", new BigDecimal("45.22231131"), new BigDecimal("47.12355151"), "US", "Texas"),
                new FoundLocationDto("Moscow", new BigDecimal("35.22212331"), new BigDecimal("27.12355151"), "FR", "Universe"),
                new FoundLocationDto("Moscow", new BigDecimal("25.22123321"), new BigDecimal("17.12355151"), "GD", "Something"),
                new FoundLocationDto("Moscow", new BigDecimal("15.21231231"), new BigDecimal("57.12123551"), "FT", "Mars"),
                new FoundLocationDto("Moscow", new BigDecimal("25.22123321"), new BigDecimal("17.12355151"), "GD", "Something")
        };
        ResponseEntity<FoundLocationDto[]> response = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito.doReturn(response).when(httpClient).getData(url, FoundLocationDto[].class);

        List<FoundLocationDto> locations = openWeatherService.findLocations(foundLocationDto);

        Assertions.assertEquals(6, locations.size());

    }
}
