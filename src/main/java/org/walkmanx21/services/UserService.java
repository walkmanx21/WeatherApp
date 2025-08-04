package org.walkmanx21.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;
import org.walkmanx21.util.PasswordEncryptionUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService {

    private final UserDao userDao;
    private final MappingUtil mappingUtil;
    private final PasswordEncryptionUtil passwordEncryptionUtil;
    private final SessionDao sessionDao;
    private final SessionService sessionService;

    @Autowired
    public UserService(UserDao userDao, MappingUtil mappingUtil, PasswordEncryptionUtil passwordEncryptionUtil, SessionDao sessionDao, SessionService sessionService) {
        this.userDao = userDao;
        this.mappingUtil = mappingUtil;
        this.passwordEncryptionUtil = passwordEncryptionUtil;
        this.sessionDao = sessionDao;
        this.sessionService = sessionService;
    }

    public void insertUser(UserRequestDto userRequestDto) {
        User user = mappingUtil.convertToUser(userRequestDto);
        String hashPassword = passwordEncryptionUtil.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        userDao.insertUser(user);
    }

    public Optional<UserResponseDto> userAuthorization(UserRequestDto userRequestDto) {
        User user = mappingUtil.convertToUser(userRequestDto);
        Optional<User> mayBeUser = userDao.getUser(user);

        if (mayBeUser.isPresent()) {
            User foundUser = mayBeUser.get();

            if (passwordEncryptionUtil.verifyPassword(user.getPassword(), foundUser.getPassword())) {
                user.setId(foundUser.getId());
                UUID sessionId = sessionService.createSession(user);

                UserResponseDto userResponseDto = mappingUtil.convertToUserResponseDto(user);
                userResponseDto.setSessionId(sessionId);

                return Optional.of(userResponseDto);
            }
        }

        System.out.println();

        return Optional.empty();


    }
}
