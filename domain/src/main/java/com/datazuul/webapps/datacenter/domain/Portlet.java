package com.datazuul.webapps.datacenter.domain;

import java.util.Locale;

/**
 * A application of the portal (e.g. "Music", "Contacts")
 *
 * @author ralf
 */
public interface Portlet extends Configurable, UniqueIdentifiable {
    String getTitle(Locale locale);
}
