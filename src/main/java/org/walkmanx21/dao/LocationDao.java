package org.walkmanx21.dao;

import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.loader.ast.spi.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.models.Location;

@Component
public class LocationDao {

    private final SessionFactory sessionFactory;


    @Autowired
    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Location insertLocation (Location location) {
        var hibernateSession = sessionFactory.getCurrentSession();
        try {
            hibernateSession.persist(location);
        } catch (JDBCConnectionException e) {
            throw new StorageUnavailableException("Storage Unavailable");
        }
        return null;
    }


}
