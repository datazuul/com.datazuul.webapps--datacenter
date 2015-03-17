package com.datazuul.webapps.datacenter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration for Business.
 *
 * @author ralf
 */
@Configuration //Marks this class as configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.datazuul.webapps.datacenter.business"
}) // needed to find Service-annotated classes
public class SpringConfigBusiness {

}
