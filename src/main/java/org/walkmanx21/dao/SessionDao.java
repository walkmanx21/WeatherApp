package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walkmanx21.models.Session;

@Component
public class SessionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public SessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void insertSession(Session session) {

    }
}
