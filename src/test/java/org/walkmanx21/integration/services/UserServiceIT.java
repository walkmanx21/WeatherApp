package org.walkmanx21.integration.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.walkmanx21.config.DataSourceConfig;
import org.walkmanx21.config.FlywayConfig;
import org.walkmanx21.config.PropertyConfig;
import org.walkmanx21.dao.UserDao;
import org.walkmanx21.dto.UserRequestDto;
import org.walkmanx21.services.UserService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataSourceConfig.class, FlywayConfig.class, PropertyConfig.class})
@ActiveProfiles("test")
@Sql({"classpath:sql/data.sql"})
public class UserServiceIT {

    private final UserRequestDto userRequestDto = new UserRequestDto("Sergey", "123456", "123456");

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void registerUser() {
        userService.registerUser(userRequestDto);


    }


    @AfterEach
    void afterEach() {

    }

    @AfterAll
    static void afterAll() {

    }
}
