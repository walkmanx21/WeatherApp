package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserRequestDto;

@Component
public class UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void insertUser(UserRequestDto userRequestDto) {
        System.out.println();
    }
}
