package org.walkmanx21.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.models.User;

import java.util.List;
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
    public User getUser(String login) {
        String hql = "FROM User WHERE login = '" + login + "'";

        Session session = sessionFactory.getCurrentSession();
        User user;

        try {
            user = session.createSelectionQuery(hql, User.class).getSingleResult();
        } catch (NoResultException e) {
            throw new UserDoesNotExistException("User with this username was not found.");
        }

        return user;
    }

    @Transactional
    public User getUser(int userId) {
        String hql = "SELECT u FROM User u WHERE u.id = :userId";

        Session hibernateSession = sessionFactory.getCurrentSession();
        User user;

        try {
            Query query = hibernateSession.createQuery(hql);
            query.setParameter("userId", userId);
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserDoesNotExistException("User with this username was not found.");
        }

        return user;
    }
}
