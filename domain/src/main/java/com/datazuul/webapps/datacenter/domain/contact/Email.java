package com.datazuul.webapps.datacenter.domain.contact;

/**
 * An email address.
 * @author ralf
 */
public interface Email {

    String getAddress();

    Contact getContact();

    Long getId();

    EmailType getType();

    void setAddress(String address);

    void setContact(Contact contact);

    void setId(Long id);

    void setType(EmailType type);

}
