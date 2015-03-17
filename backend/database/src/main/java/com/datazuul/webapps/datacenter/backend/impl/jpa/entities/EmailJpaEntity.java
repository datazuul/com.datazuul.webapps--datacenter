package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Email;
import com.datazuul.webapps.datacenter.domain.contact.EmailType;
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
 * Email address.
 *
 * @author ralf
 */
@Entity
@Table(name = "emails")
public class EmailJpaEntity implements Email {

    @Id
    @GeneratedValue
    private Long id;

    private String address;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    @ManyToOne
    @JoinColumn(name = "contact_id")
//  , insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    private ContactJpaEntity contact;

    public EmailJpaEntity() {
    }

    public EmailJpaEntity(String address, EmailType type) {
        this.address = address;
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
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public EmailType getType() {
        return type;
    }

    @Override
    public void setType(EmailType type) {
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
