package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.MutationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.models.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
public class SessionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public SessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void insertSession(Session session) {
        var hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.persist(session);
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }
    }

    public Optional<Session> getCurrentSession(UUID sessionId) {

        String hql = "SELECT s FROM Session s WHERE s.id = :sessionId";
        var hibernateSession = sessionFactory.getCurrentSession();

        try {
            var selectionQuery = hibernateSession.createSelectionQuery(hql, Session.class);
            selectionQuery.setParameter("sessionId", sessionId);
            List<Session> findSessions = selectionQuery.getResultList();
            return findSessions.stream()
                    .filter(session -> session.getLocalDateTime().isAfter(LocalDateTime.now()))
                    .findFirst();
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }
    }

    public void deleteExpiredSessions(){
        String hql = "DELETE Session s WHERE s.localDateTime < :now";
        var hibernateSession = sessionFactory.getCurrentSession();

        try {
            MutationQuery mutationQuery = hibernateSession.createMutationQuery(hql);
            mutationQuery.setParameter("now", LocalDateTime.now());
            mutationQuery.executeUpdate();

        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }
    }
}
