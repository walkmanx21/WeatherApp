package org.walkmanx21.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.walkmanx21.dao.LocationDao;
import org.walkmanx21.dto.FoundLocationDto;
import org.walkmanx21.models.Location;
import org.walkmanx21.models.User;
import org.walkmanx21.util.MappingUtil;

@Component
public class LocationService {


    @Value("${api.key}")
    private String apiKey;

    private static final int LIMIT = 5;

    private final MappingUtil mappingUtil;
    private final LocationDao locationDao;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public LocationService(MappingUtil mappingUtil, LocationDao locationDao) {
        this.mappingUtil = mappingUtil;
        this.locationDao = locationDao;
    }


    public FoundLocationDto[] findLocations (FoundLocationDto foundLocationDto) {
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + foundLocationDto.getName() + "&limit=" + LIMIT + "&appid=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<FoundLocationDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, FoundLocationDto[].class);

        return response.getBody();
    }

    public void addLocation(FoundLocationDto foundLocationDto, User user) {
        Location location = mappingUtil.convertToLocation(foundLocationDto);
        location.setUser(user);
        locationDao.insertLocation(location);
        System.out.println(location);

        System.out.println();

    }
}
