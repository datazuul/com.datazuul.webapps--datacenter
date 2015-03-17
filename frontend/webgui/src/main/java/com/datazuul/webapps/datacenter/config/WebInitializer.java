package com.datazuul.webapps.datacenter.config;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.datatables.core.web.filter.DatatablesFilter;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import static org.springframework.web.servlet.support.AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;

/**
 * Replacement for "web.xml" file.
 *
 * @author ralf
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        ServletRegistration.Dynamic servletRegistration = (ServletRegistration.Dynamic) servletContext.getServletRegistration(DEFAULT_SERVLET_NAME);
        servletRegistration.setMultipartConfig(new MultipartConfigElement("/tmp", 1024 * 1024 * 5, 1024 * 1024 * 5 * 5, 1024 * 1024));

        // Register the Dandelion filter
        FilterRegistration.Dynamic dandelionFilter = servletContext.addFilter("dandelionFilter", new DandelionFilter());
        dandelionFilter.addMappingForUrlPatterns(null, false, "/*");

        // Dandelion-Datatables filter, used for basic export
        FilterRegistration.Dynamic datatablesFilter = servletContext.addFilter("datatablesFilter", new DatatablesFilter());
        datatablesFilter.addMappingForUrlPatterns(null, false, "/*");

        // Register the Dandelion servlet
        ServletRegistration.Dynamic dandelionServlet = servletContext.addServlet("dandelionServlet", new DandelionServlet());
        dandelionServlet.setLoadOnStartup(2);
        dandelionServlet.addMapping("/dandelion-assets/*");
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");

        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        openEntityManagerInViewFilter.setEntityManagerFactoryBeanName("entityManagerFactory");

        return new Filter[]{openEntityManagerInViewFilter, characterEncodingFilter};
    }
}
