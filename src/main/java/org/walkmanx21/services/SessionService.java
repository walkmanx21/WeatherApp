package org.walkmanx21.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.dao.SessionDao;
import org.walkmanx21.models.Session;
import org.walkmanx21.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SessionService {

    private final SessionDao sessionDao;

    @Autowired
    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public UUID createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusSeconds(24*60*60));
        sessionDao.insertSession(session);

        return session.getId();
    }
}
