package com.datazuul.webapps.datacenter.backend.api;

import com.datazuul.webapps.datacenter.domain.contact.Address;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Email;
import com.datazuul.webapps.datacenter.domain.contact.Phone;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author ralf
 * @param <T>
 * @param <ID>
 */
public interface ContactRepository<T extends Contact, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, FilterableRepository<T> {

    List<T> findByEmail(String address);

    public Contact createContact();

    public Address createAddress();

    public Phone createPhone();

    public Email createEmail();
}
