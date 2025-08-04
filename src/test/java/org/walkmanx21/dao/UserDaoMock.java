package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.walkmanx21.models.User;

import java.util.Optional;

public class UserDaoMock extends UserDao {

    public UserDaoMock(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void insertUser(User user) {
        super.insertUser(user);
    }

    @Override
    public Optional<User> getUser(User incomingUser) {
        return super.getUser(incomingUser);
    }
}
