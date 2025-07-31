package org.walkmanx21.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.user}")
    private String databaseUser;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${db.hikari.maximumPoolSize}")
    private int maxPoolSize;

    @Value("${db.hikari.minimumIdle}")
    private int minIdle;

    @Value("${db.hikari.idleTimeout}")
    private long idleTimeout;

    @Value("${db.hikari.poolName}")
    private String poolName;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(databaseDriver);
        config.setJdbcUrl(databaseUrl);
        config.setUsername(databaseUser);
        config.setPassword(databasePassword);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(idleTimeout);
        config.setPoolName(poolName);

        return new HikariDataSource(config);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}
