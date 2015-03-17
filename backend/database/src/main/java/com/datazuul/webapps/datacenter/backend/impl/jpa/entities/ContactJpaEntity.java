package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

import com.datazuul.webapps.datacenter.domain.contact.Address;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Email;
import com.datazuul.webapps.datacenter.domain.contact.EmailType;
import com.datazuul.webapps.datacenter.domain.contact.Phone;
import com.datazuul.webapps.datacenter.domain.contact.PhoneType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Contact domain object.
 *
 * @author ralf
 */
@Entity
@Table(name = "contacts")
public class ContactJpaEntity implements Contact {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 1, max = 255)
    private String firstname;

    @NotNull
    @Size(min = 1, max = 255)
    private String lastname;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = AddressJpaEntity.class)
    @JoinColumn(name = "contact_id")
//  @OrderColumn // defaults to "emails_order"
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = EmailJpaEntity.class)
    @JoinColumn(name = "contact_id")
//  @OrderColumn // defaults to "emails_order"
    @JsonManagedReference
    private List<Email> emails = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = PhoneJpaEntity.class)
    @JoinColumn(name = "contact_id")
//  @OrderColumn // defaults to "phones_order"
    @JsonManagedReference
    private List<Phone> phones = new ArrayList<>();

    public ContactJpaEntity() {
    }

    public ContactJpaEntity(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public List<Address> getAddresses() {
        return addresses;
    }

    @Override
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public List<Email> getEmails() {
        return emails;
    }

    @Override
    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    @Override
    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public void addAddress(Address address) {
        address.setContact(this);
        getAddresses().add(address);
    }

    @Override
    public void addEmail(Email email) {
        email.setContact(this);
        getEmails().add(email);
    }

    @Override
    public void addEmail(String address, EmailType type) {
        final EmailJpaEntity email = new EmailJpaEntity(address, type);
        addEmail(email);
    }

    @Override
    public void addPhone(Phone phone) {
        phone.setContact(this);
        getPhones().add(phone);
    }

    @Override
    public void addPhone(String number, PhoneType type) {
        final PhoneJpaEntity phone = new PhoneJpaEntity(number, type);
        addPhone((Phone) phone);
    }
}
