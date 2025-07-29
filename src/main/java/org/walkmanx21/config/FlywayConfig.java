package org.walkmanx21.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)  // Источник данных (БД)
                .baselineOnMigrate(true)  // Создаёт baseline при первом запуске
                .baselineVersion("0")     // Начальная версия миграций
                .validateOnMigrate(false) // Отключает валидацию (для гибкости)
                .outOfOrder(true)         // Разрешает применять миграции не по порядку
                .load();
    }
}
