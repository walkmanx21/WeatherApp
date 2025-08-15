package org.walkmanx21.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.*;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;

@Component
public class MappingUtil {

    private static final ModelMapper MODEL_MAPPER;




    static {
        MODEL_MAPPER = new ModelMapper();

        PropertyMap<OpenWeatherResponseDto, WeatherResponseDto> weatherMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setCountry(source.getSys().get("country"));
                map().setLatitude(source.getCoord().get("lat"));
                map().setLongitude(source.getCoord().get("lon"));
                long temperature = Math.round(source.getMain().get("temp"));
                map().setTemperature(temperature + "°C");
                long feelsLike = Math.round(source.getMain().get("feels_like"));
                map().setFeelsLike(feelsLike + "°C");
                map().setDescription(capitalizeFirstLetter(source.getWeather()[0].get("description")));
                map().setHumidity(source.getMain().get("humidity").toString() + "%");
                map().setWeatherIcon(source.getWeather()[0].get("icon"));
            }
        };

        MODEL_MAPPER.addMappings(weatherMap);
    }

    public User convertToUser(UserRequestDto userRequestDto) {
        return MODEL_MAPPER.map(userRequestDto, User.class);
    }

    public UserResponseDto convertToUserResponseDto(User user) {
        return MODEL_MAPPER.map(user, UserResponseDto.class);
    }

    public Location convertToLocation (FoundLocationDto foundLocationDto) {
        return MODEL_MAPPER.map(foundLocationDto, Location.class);
    }

    public Location convertToLocation (WeatherResponseDto weatherResponseDto) {
        return MODEL_MAPPER.map(weatherResponseDto, Location.class);
    }

    public WeatherResponseDto convertToWeatherResponseDto(OpenWeatherResponseDto openWeatherResponseDto) {
        return MODEL_MAPPER.map(openWeatherResponseDto, WeatherResponseDto.class);
    }

    private static String capitalizeFirstLetter(String str) {
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

}
