package org.walkmanx21.integration.services;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.config.DataSourceConfig;
import org.walkmanx21.config.FlywayConfig;
import org.walkmanx21.config.PropertyConfig;
import org.walkmanx21.dao.LocationDao;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.services.LocationService;
import org.walkmanx21.services.OpenWeatherService;
import org.walkmanx21.util.HttpClientUtil;
import org.walkmanx21.util.MappingUtil;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class LocationServiceIT {

    private final FoundLocationDto foundLocationDto = new FoundLocationDto();
    private final Location location = new Location(1, "Moscow", new User(), new BigDecimal("55.7504461"), new BigDecimal("37.6174943"), "RU", "");

    @Autowired
    private LocationService locationService;

    private final OpenWeatherService openWeatherService = Mockito.mock(OpenWeatherService.class);


    @BeforeEach
    void prepare() {
        foundLocationDto.setName("Moscow");
    }

    @Test
    void whenFindLocationByNameMoscowThenReturn2Locations() {
        Mockito.doReturn(
                List.of(new FoundLocationDto("Moscow", new BigDecimal("55.22231131"), new BigDecimal("37.12355151"), "RU", "Moscow obl"),
                        new FoundLocationDto("Moscow", new BigDecimal("45.22231131"), new BigDecimal("47.12355151"), "US", "Texas"),
                        new FoundLocationDto("Moscow", new BigDecimal("35.22212331"), new BigDecimal("27.12355151"), "FR", "Universe"),
                        new FoundLocationDto("Moscow", new BigDecimal("25.22123321"), new BigDecimal("17.12355151"), "GD", "Something"),
                        new FoundLocationDto("Moscow", new BigDecimal("15.21231231"), new BigDecimal("57.12123551"), "FT", "Mars"))
        ).when(openWeatherService).findLocations(foundLocationDto);

        List<FoundLocationDto> foundLocationDtoList = locationService.findLocations(foundLocationDto);

        Assertions.assertNotNull(foundLocationDtoList);
        Assertions.assertEquals(5, foundLocationDtoList.size());
        Assertions.assertEquals("Moscow", foundLocationDtoList.get(0).getName());
        Assertions.assertEquals("Moscow", foundLocationDtoList.get(1).getName());
    }

    @Test
    void test2() {
        Mockito.doReturn(new WeatherResponseDto(
                1, "Moscow", "RU", new BigDecimal("55.7504461"), new BigDecimal("37.6174943"),
                "27 C", "29 C", "Cloudly", "70%", "04d"))
                .when(openWeatherService).getWeatherData(location);

        WeatherResponseDto weatherResponseDto = locationService.getWeatherData(location);

        Assertions.assertNotNull(weatherResponseDto);
        Assertions.assertEquals(location.getName(), weatherResponseDto.getName());
    }
}
