package org.walkmanx21.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.loader.ast.spi.Loader;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.dto.WeatherResponseDto;
import org.walkmanx21.exceptions.LocationAlreadyExistException;
import org.walkmanx21.exceptions.StorageUnavailableException;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;

import java.util.List;

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
        } catch (ConstraintViolationException e) {
            throw new LocationAlreadyExistException("Location is already exists");
        }
        return null;
    }

    @Transactional
    public List<Location> getAllUserLocations(User user) {
        var hibernateSession = sessionFactory.getCurrentSession();
        String hql = "SELECT l FROM Location l WHERE l.user.id = :userId";
        var selectionQuery = hibernateSession.createSelectionQuery(hql, Location.class);
        selectionQuery.setParameter("userId", user.getId());
        return selectionQuery.getResultList();
    }


}
