package org.walkmanx21.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {

    private int id;
    private String name;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int temperature;
    private int feelsLike;
    private String description;
    private String humidity;
    private String weatherIcon;

}
