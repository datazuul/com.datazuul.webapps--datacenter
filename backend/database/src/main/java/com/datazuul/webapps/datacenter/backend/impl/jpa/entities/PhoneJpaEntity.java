package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Phone;
import com.datazuul.webapps.datacenter.domain.contact.PhoneType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Phone.
 *
 * @author ralf
 */
@Entity
@Table(name = "phones")
public class PhoneJpaEntity implements Phone {

    @Id
    @GeneratedValue
    private Long id;

    private String number;

    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @ManyToOne
    @JoinColumn(name = "contact_id")
//          , insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    private ContactJpaEntity contact;

    public PhoneJpaEntity() {
    }

    public PhoneJpaEntity(String number, PhoneType type) {
        this.number = number;
        this.type = type;
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
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public PhoneType getType() {
        return type;
    }

    @Override
    public void setType(PhoneType type) {
        this.type = type;
    }

    @Override
    public ContactJpaEntity getContact() {
        return contact;
    }

    @Override
    public void setContact(Contact contact) {
        this.contact = (ContactJpaEntity) contact;
    }
}
