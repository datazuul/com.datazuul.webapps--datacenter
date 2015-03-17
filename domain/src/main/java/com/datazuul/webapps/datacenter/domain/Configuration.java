package com.datazuul.webapps.datacenter.domain;

/**
 * Settings of a Portlet
 *
 * @author ralf
 */
public interface Configuration {

    Configurable getParent();

    String get(String key);
}
