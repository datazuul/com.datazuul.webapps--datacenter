package com.datazuul.webapps.datacenter.domain.contact;

/**
 * Phone.
 *
 * @author ralf
 */
public interface Phone {

    Contact getContact();

    Long getId();

    String getNumber();

    PhoneType getType();

    void setContact(Contact contact);

    void setId(Long id);

    void setNumber(String number);

    void setType(PhoneType type);

}
