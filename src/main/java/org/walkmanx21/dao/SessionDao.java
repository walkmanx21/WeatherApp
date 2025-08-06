package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.models.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Optional<Session> getCurrentSession(int userId) {
        var hibernateSession = sessionFactory.getCurrentSession();
        String hql = "SELECT s FROM Session s WHERE s.user.id = :userId";
        Query query = hibernateSession.createQuery(hql);
        query.setParameter("userId", userId);
        List<Session> findSessions = query.getResultList();
        Optional<Session> mayBeCurrentSession = findSessions.stream()
                .filter(session -> session.getLocalDateTime().isAfter(LocalDateTime.now()))
                .findFirst();

        return mayBeCurrentSession;
    }
}
