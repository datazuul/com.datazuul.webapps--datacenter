package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

//import com.datazuul.framework.domain.geo.Country;
import com.datazuul.webapps.datacenter.domain.contact.Address;
import com.datazuul.webapps.datacenter.domain.contact.AddressType;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
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
@Table(name = "addresses")
public class AddressJpaEntity implements Address {

    @Id
    @GeneratedValue
    private Long id;

    private String city;

//  @Enumerated(EnumType.STRING)
//  private Country country;
    private String housenumber;
    private Double latitude;
    private Double longitude;
    private String street;
    private String zipcode;

    @Enumerated(EnumType.STRING)
    private AddressType type;

    @ManyToOne
    @JoinColumn(name = "contact_id")
//  , insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    private ContactJpaEntity contact;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

//  public Country getCountry() {
//    return country;
//  }
//
//  public void setCountry(Country country) {
//    this.country = country;
//  }
    @Override
    public String getHousenumber() {
        return housenumber;
    }

    @Override
    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getZipcode() {
        return zipcode;
    }

    @Override
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public AddressType getType() {
        return type;
    }

    @Override
    public void setType(AddressType type) {
        this.type = type;
    }

    @Override
    public Contact getContact() {
        return (Contact) contact;
    }

    @Override
    public void setContact(Contact contact) {
        this.contact = (ContactJpaEntity) contact;
    }

}
