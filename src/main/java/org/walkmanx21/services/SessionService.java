package org.walkmanx21.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Value("${lifetime.duration}")
    private long sessionLifetime;

    @Autowired
    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public Session createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusSeconds(sessionLifetime));
        sessionDao.insertSession(session);
        return session;
    }

    public Optional<Session> getCurrentSession(UUID sessionId) {
        return sessionDao.getCurrentSession(sessionId);
    }

    @Scheduled(fixedRateString = "1d")
    public void cleanExpiredSessions() {
        sessionDao.deleteExpiredSessions();
    }
}
