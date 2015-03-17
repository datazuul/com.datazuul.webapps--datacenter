package com.datazuul.webapps.datacenter.business;

import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import java.io.InputStream;
import java.util.List;
import org.springframework.validation.Errors;

/**
 * Service for Contacts.
 *
 * @author ralf
 */
public interface ContactService {

    Iterable<Contact> getAll();

    /**
     * Query used to populate the DataTables that display the list of results.
     *
     * @param criterias The DataTables criterias used to filter the results
     * (maxResult, filtering, paging, ...)
     * @return a filtered list of results.
     */
    DataSet<Contact> findByDatatablesCriterias(DatatablesCriterias criterias);

    /**
     * @param maxResult Max number of results.
     * @return a maxResult limited list of results.
     */
    List<? extends Contact> findLimited(int maxResult);

    boolean save(Contact contact, Errors errors);

    Contact get(Long id);

    Contact delete(Long id);

    void importContacts(InputStream inputStream, String originalFilename);

    public Contact createContact();
}
