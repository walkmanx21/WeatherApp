package org.walkmanx21.dao;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.exceptions.UserAlreadyExistException;
import org.walkmanx21.exceptions.UserDoesNotExistException;
import org.walkmanx21.models.User;

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
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
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
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }

        return user;
    }

    @Transactional
    public User getUser(int userId) {
        String hql = "SELECT u FROM User u WHERE u.id = :userId";

        Session hibernateSession = sessionFactory.getCurrentSession();
        User user;

        try {
            var selectionQuery = hibernateSession.createSelectionQuery(hql, User.class);
            selectionQuery.setParameter("userId", userId);
            user = selectionQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new UserDoesNotExistException("User with this username was not found.");
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }

        return user;
    }
}
