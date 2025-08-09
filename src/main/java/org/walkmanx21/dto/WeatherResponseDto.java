package org.walkmanx21.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponseDto {

    private String name;
    private String country;
    private String temperature;
    private String feelsLike;
    private String description;
    private String humidity;

}
