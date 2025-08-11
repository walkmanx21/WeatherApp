package org.walkmanx21.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherResponseDto {

    private int id;
    private String name;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String temperature;
    private String feelsLike;
    private String description;
    private String humidity;
    private String weatherIcon;

}
