package org.walkmanx21.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;
import org.walkmanx21.util.CookieUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionService {

    private final SessionDao sessionDao;
    private final CookieUtil cookieUtil;

    @Value("${lifetime.duration}")
    private long sessionLifetime;

    @Autowired
    public SessionService(SessionDao sessionDao, CookieUtil cookieUtil) {
        this.sessionDao = sessionDao;
        this.cookieUtil = cookieUtil;
    }

    public Session createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusSeconds(sessionLifetime));
        sessionDao.insertSession(session);
        return session;
    }

    public Optional<Session> getCurrentSession(UUID sessionId) {
        return sessionDao.getCurrentSession(sessionId);
    }

    public void deleteSessionId(HttpServletResponse response) {
        cookieUtil.deleteSessionId(response);
    }

    @Scheduled(fixedRateString = "1d")
    public void cleanExpiredSessions() {
        sessionDao.deleteExpiredSessions();
    }
}
