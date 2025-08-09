package org.walkmanx21.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.walkmanx21.dto.FoundLocationDto;

@Component
public class HttpClientUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public <R> ResponseEntity<R> getData(String url, Class<R> responseType) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

    }



}
