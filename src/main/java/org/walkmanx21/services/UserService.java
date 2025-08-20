package org.walkmanx21.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.ResponseDto;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.exceptions.WrongPasswordException;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;
import org.walkmanx21.util.PasswordEncryptionUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@Setter
public class UserService {

    private final UserDao userDao;
    private final MappingUtil mappingUtil;
    private final PasswordEncryptionUtil passwordEncryptionUtil;
    private final SessionService sessionService;

    @Autowired
    public UserService(UserDao userDao, MappingUtil mappingUtil, PasswordEncryptionUtil passwordEncryptionUtil, SessionService sessionService) {
        this.userDao = userDao;
        this.mappingUtil = mappingUtil;
        this.passwordEncryptionUtil = passwordEncryptionUtil;
        this.sessionService = sessionService;
    }

    public ResponseDto registerUser(UserRequestDto userRequestDto) {
        User user = mappingUtil.convertToUser(userRequestDto);
        String hashPassword = passwordEncryptionUtil.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        ResponseDto responseDto;
        try {
            user = userDao.insertUser(user);
            Session session = sessionService.createSession(user);
            responseDto = mappingUtil.convertToResponseDto(user);
            responseDto.setSessionId(session.getId());
        } catch (UserAlreadyExistException e) {
            responseDto = new ResponseDto(true, "login", e.getMessage());
        }
        return responseDto;
    }

    public ResponseDto authorizeUser(UserRequestDto userRequestDto) {
        User incomingUser = mappingUtil.convertToUser(userRequestDto);
        ResponseDto responseDto;

        try {
            User foundUser = userDao.getUser(incomingUser.getLogin());
            passwordEncryptionUtil.verifyPassword(incomingUser.getPassword(), foundUser.getPassword());
            incomingUser.setId(foundUser.getId());
            Session session = sessionService.createSession(incomingUser);
            responseDto = mappingUtil.convertToResponseDto(incomingUser);
            responseDto.setSessionId(session.getId());
        } catch (UserDoesNotExistException e) {
            responseDto = new ResponseDto(true, "login", e.getMessage());
        } catch (WrongPasswordException e) {
            responseDto = new ResponseDto(true, "password", e.getMessage());
        }

        return responseDto;
    }

    public User getUser(int userId) {
        return userDao.getUser(userId);
    }

    public Optional<User> getUserByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> mayBeCookie = Optional.empty();
        if (cookies != null) {
            mayBeCookie = Arrays.stream(cookies).filter(c -> c.getName().equals("sessionId"))
                    .findFirst();
        }

        if (mayBeCookie.isPresent()) {
            UUID sessionId = UUID.fromString(mayBeCookie.get().getValue());
            Optional<Session> mayBeSession = sessionService.getCurrentSession(sessionId);
            if (mayBeSession.isPresent()) {
                Session session = mayBeSession.get();
                return Optional.of(getUser(session.getUser().getId()));
            }
        }
        return Optional.empty();
    }


}
