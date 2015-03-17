package com.datazuul.webapps.datacenter.config;

import com.datazuul.webapps.datacenter.frontend.webgui.controller.ContactsController;
import com.datazuul.webapps.datacenter.frontend.webgui.controller.MusicController;
import com.datazuul.webapps.datacenter.frontend.webgui.controller.PortalController;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesCriteriasMethodArgumentResolver;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.thymeleaf.dialect.DandelionDialect;
import java.util.List;
import java.util.Locale;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/**
 * Configuration for Web.
 *
 * @author ralf
 */
@ComponentScan(basePackages = {"com.github.dandelion.datatables.web"})
@Configuration //Marks this class as configuration
@EnableWebMvc //Enables Spring's MVC annotations
@Import({SpringConfigRepository.class, SpringConfigBusiness.class})
public class SpringConfigWeb extends WebMvcConfigurerAdapter {

    /*
     * Handles HTTP GET requests for /resources/** by efficiently serving up static
     * resources in the ${symbol_dollar}{webappRoot}/resources directory
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/img/favicon.ico");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");

        // module specifics
        registry.addResourceHandler("/music/**").addResourceLocations("/music/");
    }

//    @Bean
//    public UrlBasedViewResolver configureViewResolver() {
//        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//        resolver.setPrefix("/WEB-INF/jsp/");
//        resolver.setSuffix(".jsp");
//        // Specialization of InternalResourceView for JSTL pages,
//        // i.e. JSP pages that use the JSP STandard Tag Library:
//        resolver.setViewClass(JstlView.class);
//        return resolver;
//    }
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/thymeleaf/");
        resolver.setSuffix(".xhtml");
        resolver.setTemplateMode("HTML5");
        // resolver.setOrder(1);
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        // Activate Thymeleaf LayoutDialect[1] (for 'layout'-namespace)
        // [1] https://github.com/ultraq/thymeleaf-layout-dialect
        engine.addDialect(new LayoutDialect());
//        engine.addDialect(new SpringStandardDialect());
//        engine.addDialect(new SpringSecurityDialect());
        engine.addDialect(new DandelionDialect());
        engine.addDialect(new DataTablesDialect());
        return engine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }

    @Bean
    public ContactsController contactsController() {
        ContactsController contactsController = new ContactsController();
        contactsController.setConfirmationViewName("created");
        return contactsController;
    }

    @Bean
    public MusicController musicController() {
        return new MusicController();
    }

    @Bean
    public PortalController portalController() {
        return new PortalController();
    }

    /**
     * Create a resource bundle for your messages ("messages.properties"). This file goes in src/main/resources because
     * you want it to appear at the root of the classpath on deployment.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public MessageSource configureMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(5);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver sessionLocaleResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.GERMAN);
        return localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(5 * 1024 * 1024);
        return resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DatatablesCriteriasMethodArgumentResolver());
    }
}
