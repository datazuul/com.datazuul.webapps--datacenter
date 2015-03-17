package com.datazuul.webapps.datacenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration for application.
 *
 * @author ralf
 */
@Configuration //Marks this class as configuration
//@ComponentScan(basePackages = "com.datazuul.webapps.datacenter.contacts") // needed to find Controller-annotated classes
@Import({SpringConfigWeb.class})
public class AppConfig {
    
}
