package org.walkmanx21.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.models.User;

@Component
public class MappingUtil {

    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
        MODEL_MAPPER.typeMap(UserRequestDto.class, User.class)
                .addMapping(UserRequestDto::getUsername, User::setLogin);
    }

    public User convertToUser(UserRequestDto userRequestDto) {
        return MODEL_MAPPER.map(userRequestDto, User.class);
    }

}
