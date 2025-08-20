package org.walkmanx21.integration.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.config.*;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.models.User;
import org.walkmanx21.services.UserService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

    private final UserRequestDto userRequestDto = new UserRequestDto("walkmanx21", "walkmanx21", "walkmanx21");
    private UserResponseDto userResponseDto;

    @Autowired
    private UserService userService;

    @BeforeEach
    void prepare() {
        userResponseDto = userService.registerUser(userRequestDto);
    }

    @Test
    void whenRegisterNewUserThenRequestUserFromDatabaseByIdLoginsMatch() {
        User user = userService.getUser(userResponseDto.getUserId());
        Assertions.assertEquals(userRequestDto.getLogin(), userResponseDto.getLogin());
        Assertions.assertNotNull(user);
    }

    @Test
    void whenRegisterAlreadyExistUserThenThrowException() {
        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(userRequestDto));
    }

    @Test
    void whenAuthorizationWithWrongPasswordThenThrowException() {
        userRequestDto.setPassword("walkmanx22");
        Assertions.assertThrows(WrongPasswordException.class, () -> userService.authorizeUser(userRequestDto));
    }

    @Test
    void whenNonExistentUserLoginThenThrowException() {
        userRequestDto.setLogin("walkmanx22");
        Assertions.assertThrows(UserDoesNotExistException.class, () -> userService.authorizeUser(userRequestDto));
    }
}
