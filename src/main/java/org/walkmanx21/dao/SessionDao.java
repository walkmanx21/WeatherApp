package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.models.Session;

@Component
public class SessionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public SessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void insertSession(Session session) {
        var hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.persist(session);
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }
    }
}
