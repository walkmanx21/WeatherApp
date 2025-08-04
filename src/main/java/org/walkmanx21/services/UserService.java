package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;
import org.walkmanx21.util.PasswordEncryptionUtil;

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

    public void registerUser(UserRequestDto userRequestDto) {
        User user = mappingUtil.convertToUser(userRequestDto);
        String hashPassword = passwordEncryptionUtil.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        userDao.insertUser(user);
    }

    public Optional<UserResponseDto> authorizeUser(UserRequestDto userRequestDto) {
        User user = mappingUtil.convertToUser(userRequestDto);
        Optional<User> mayBeUser = userDao.getUser(user);
        Optional<UserResponseDto> mayBeUSerResponseDto = Optional.empty();

        if (mayBeUser.isPresent()) {
            User foundUser = mayBeUser.get();

            if (passwordEncryptionUtil.verifyPassword(user.getPassword(), foundUser.getPassword())) {
                user.setId(foundUser.getId());
                UUID sessionId = sessionService.createSession(user);

                UserResponseDto userResponseDto = mappingUtil.convertToUserResponseDto(user);
                userResponseDto.setSessionId(sessionId);
                mayBeUSerResponseDto = Optional.of(userResponseDto);
            } else {
                throw new WrongPasswordException("The entered password is incorrect");
            }
        }

        return mayBeUSerResponseDto;
    }
}
