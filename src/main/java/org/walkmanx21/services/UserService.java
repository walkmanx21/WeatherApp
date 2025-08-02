package org.walkmanx21.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.UserResponseDto;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;
import org.walkmanx21.util.PasswordEncryptionUtil;

import java.util.Optional;

@Component
public class UserService {

    private final UserDao userDao;
    private final MappingUtil mappingUtil;
    private final PasswordEncryptionUtil passwordEncryptionUtil;

    @Autowired
    public UserService(UserDao userDao, MappingUtil mappingUtil, PasswordEncryptionUtil passwordEncryptionUtil) {
        this.userDao = userDao;
        this.mappingUtil = mappingUtil;
        this.passwordEncryptionUtil = passwordEncryptionUtil;
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
                UserResponseDto userResponseDto = mappingUtil.convertToUserResponseDto(user);
                return Optional.of(userResponseDto);
            }
        }

        System.out.println();

        return Optional.empty();


    }
}
