package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionService {

    private final SessionDao sessionDao;
    private static final long SESSION_LIFETIME = 24 * 60 * 60;

    @Autowired
    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public Session createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusSeconds(SESSION_LIFETIME));
        sessionDao.insertSession(session);
        return session;
    }

    public Optional<Session> getCurrentSession(UUID sessionId) {
        return sessionDao.getCurrentSession(sessionId);
    }
}
