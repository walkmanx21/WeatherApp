package org.walkmanx21.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.*;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;

@Component
public class MappingUtil {

    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
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

}
