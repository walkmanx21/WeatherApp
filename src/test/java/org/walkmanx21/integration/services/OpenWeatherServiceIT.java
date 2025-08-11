package org.walkmanx21.integration.services;

import org.junit.jupiter.api.BeforeAll;
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
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.services.OpenWeatherService;
import org.walkmanx21.util.HttpClientUtil;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class OpenWeatherServiceIT {

    private FoundLocationDto foundLocationDto;

    @Autowired
    private OpenWeatherService openWeatherService;

    private final HttpClientUtil httpClientUtil;

    public OpenWeatherServiceIT(HttpClientUtil httpClientUtil) {
        this.httpClientUtil = Mockito.mock(HttpClientUtil.class);
    }

    @BeforeEach
    void prepare() {
        foundLocationDto.setName("Moscow");
    }

    @Test
    void test() {
        List<FoundLocationDto> foundLocationDtoList = openWeatherService.findLocations(foundLocationDto);

    }
}
