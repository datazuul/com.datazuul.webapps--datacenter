package com.datazuul.webapps.datacenter.config;

import com.datazuul.webapps.datacenter.backend.impl.jpa.ContactRepositoryImplJpa;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Configuration for Repository backend.
 *
 * @author ralf
 */
@Configuration //Marks this class as configuration
// Will scan the package of the annotated configuration class for Spring Data repositories by default.
@EnableJpaRepositories(basePackages = {
    "com.datazuul.webapps.datacenter.backend.impl.jpa"
}) // Annotation to enable JPA repositories.
@ComponentScan(basePackageClasses = {ContactRepositoryImplJpa.class}) // needed to find Repository-annotated classes
public class SpringConfigRepository {

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource()); // could be another datasource with different user/pwd...
        flyway.setLocations("classpath:/com/datazuul/webapps/datacenter/backend/repository/database/migration");
        flyway.setInitOnMigrate(true);
        return flyway;
    }

    @Bean
    public DataSource dataSource() {
//        System.setProperty("derby.system.home", "C:\\derby");
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).setName("c:/derby/contacts;create=true").build();
//        final EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).build();
//        return db;

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        String userHomeDirectory = System.getProperty("user.home");
        ds.setUrl("jdbc:derby:" + userHomeDirectory + "/.datazuul/contacts;create=true");
        ds.setUsername("");
        ds.setPassword("");
        return ds;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.DERBY);
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean
    @DependsOn(value = "flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
        lemfb.setDataSource(dataSource());
        lemfb.setJpaVendorAdapter(jpaVendorAdapter());
        lemfb.setJpaProperties(jpaProperties());
        lemfb.setPackagesToScan("com.datazuul.webapps.datacenter.backend.impl.jpa.entities");
        return lemfb;
    }

//    private Properties jpaProperties() {
//        Properties p = new Properties();
//        p.put("hibernate.hbm2ddl.auto", "update");
//        p.put("hbm2ddl.auto", "update");
//        p.put("spring.datasource.url", "jdbc:derby:c:/derby/contacts;create=true");
//        return p;
//    }
    private Properties jpaProperties() {
        Properties p = new Properties();
        // DDL is done by flyway
        p.put("hibernate.hbm2ddl.auto", "none");
        p.put("hbm2ddl.auto", "none");
        p.put("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        return p;
    }
}
