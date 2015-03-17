package com.datazuul.webapps.datacenter.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Configuration for Repository backend.
 *
 * @author ralf
 */
@Configuration //Marks this class as configuration
public class SpringConfigRepositoryTest extends SpringConfigRepository {

    @Bean
    @Override
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).build();
    }
}
