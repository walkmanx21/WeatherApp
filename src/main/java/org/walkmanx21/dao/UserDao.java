package org.walkmanx21.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.exceptions.UserDoesNotExistException;
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
    public User insertUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.persist(user);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException("User with this username already exists");
        }
        return user;
    }

    @Transactional
    public Optional<User> getUser(User incomingUser) {
        String hql = "FROM User WHERE login = '" + incomingUser.getLogin() + "'";

        Session session = sessionFactory.getCurrentSession();
        Optional<User> mayBeUser = Optional.empty();
        try {
            mayBeUser = Optional.of(session.createSelectionQuery(hql, User.class).getSingleResult());
        } catch (NoResultException e) {
            throw new UserDoesNotExistException("User with this username was not found.");
        }

        return mayBeUser;

    }
}
