package org.walkmanx21.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.models.User;

import java.util.Optional;

@Component
public class UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void insertUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.persist(user);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException("User with this username already exists");
        }
    }

    @Transactional
    public Optional<User> getUser(User incomingUser) {
        String hql = "FROM User WHERE login = '" + incomingUser.getLogin() + "'";

        Session session = sessionFactory.getCurrentSession();

        return Optional.of(session.createSelectionQuery(hql, User.class).getSingleResult());

    }
}
