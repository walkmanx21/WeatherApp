package org.walkmanx21.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:application-test.properties")
public class TestPropertyConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer () {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
