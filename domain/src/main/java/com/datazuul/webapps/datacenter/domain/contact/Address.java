package com.datazuul.webapps.datacenter.domain.contact;

/**
 * Address.
 *
 * @author ralf
 */
public interface Address {

    String getCity();

    Contact getContact();

    //  public Country getCountry() {
    //    return country;
    //  }
    //
    //  public void setCountry(Country country) {
    //    this.country = country;
    //  }
    String getHousenumber();

    Long getId();

    Double getLatitude();

    Double getLongitude();

    String getStreet();

    AddressType getType();

    String getZipcode();

    void setCity(String city);

    void setContact(Contact contact);

    void setHousenumber(String housenumber);

    void setId(Long id);

    void setLatitude(Double latitude);

    void setLongitude(Double longitude);

    void setStreet(String street);

    void setType(AddressType type);

    void setZipcode(String zipcode);

}
