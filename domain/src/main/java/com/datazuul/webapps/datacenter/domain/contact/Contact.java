package com.datazuul.webapps.datacenter.domain.contact;

import java.util.List;

/**
 * Contact.
 *
 * @author ralf
 */
public interface Contact {

    void addAddress(Address address);

    void addEmail(Email email);

    void addEmail(String address, EmailType type);

    void addPhone(Phone phone);

    void addPhone(String number, PhoneType type);

    List<Address> getAddresses();

    List<Email> getEmails();

    String getFirstname();

    Long getId();

    String getLastname();

    List<Phone> getPhones();

    void setAddresses(List<Address> addresses);

    void setEmails(List<Email> emails);

    void setFirstname(String firstname);

    void setId(Long id);

    void setLastname(String lastname);

    void setPhones(List<Phone> phones);

}
