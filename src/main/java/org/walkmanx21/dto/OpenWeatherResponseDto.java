package org.walkmanx21.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponseDto {

    private int id;
    private String name;
    private Map<String, String> sys;
    private Map<String, BigDecimal> coord;
    private Map<String, Double> main;
    private Map<String, String>[] weather;

}
