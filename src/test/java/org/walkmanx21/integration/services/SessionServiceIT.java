package org.walkmanx21.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.config.DataSourceConfig;
import org.walkmanx21.config.FlywayConfig;
import org.walkmanx21.config.PropertyConfig;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.dto.ResponseDto;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.services.SessionService;
import org.walkmanx21.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Transactional
public class SessionServiceIT {

    private final UserRequestDto userRequestDto = new UserRequestDto("walkmanx21", "walkmanx21", "walkmanx21");
    private ResponseDto userResponseDto;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void prepare() {
        userResponseDto = userService.registerUser(userRequestDto);
    }

    @Test
    void whenRegistrationTakesPlaceThenSessionIsCreated() {
        UUID sessionId = userResponseDto.getSessionId();
        Optional<Session> currentSession = sessionService.getCurrentSession(sessionId);
        Assertions.assertTrue(currentSession.isPresent());

    }

    @Test
    void whenWaitIsLongerThanLifetimeOfSessionItExpires() throws InterruptedException {
        UUID sessionId = userResponseDto.getSessionId();
        Thread.sleep(2000);
        Optional<Session> currentSession = sessionService.getCurrentSession(sessionId);
        Assertions.assertFalse(currentSession.isPresent());
    }

    @Test
    void whenCleanExpiresSessionsThanTheyBeingCleansed() {
        User user = new User(0, "walkmanx22", "123456");
        user = userDao.insertUser(user);
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().minusDays(1));
        sessionDao.insertSession(session);
        sessionService.cleanExpiredSessions();
        Assertions.assertEquals(Optional.empty(), sessionService.getCurrentSession(session.getId()));
    }

}
