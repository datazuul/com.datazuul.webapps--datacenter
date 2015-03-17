package com.datazuul.webapps.datacenter.business;

import com.datazuul.webapps.datacenter.backend.api.ContactRepository;
import com.datazuul.webapps.datacenter.domain.contact.Address;
import com.datazuul.webapps.datacenter.domain.contact.AddressType;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Email;
import com.datazuul.webapps.datacenter.domain.contact.EmailType;
import com.datazuul.webapps.datacenter.domain.contact.Phone;
import com.datazuul.webapps.datacenter.domain.contact.PhoneType;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

/**
 * Contact service implementation.
 *
 * @author ralf
 */
@Service
@Transactional(readOnly = true) // default transactional behavior
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Iterable<Contact> getAll() {
        return contactRepository.findAll(new Sort(Sort.Direction.ASC, "lastname"));
    }

    @Transactional(readOnly = false) // overrides default behavior
    @Override
    public boolean save(Contact contact, Errors errors) {
        validateIsUnique(contact, errors);
        boolean valid = !errors.hasErrors();
        if (valid) {
            contactRepository.save(contact);
        }
        return valid;
    }

    @Override
    public Contact get(Long id) {
        return (Contact) contactRepository.findOne(id);
    }

    private void validateIsUnique(Contact contact, Errors errors) {
        boolean duplicate = false;
        List<Email> emails = contact.getEmails();
        for (Email email : emails) {
            final List<Contact> contacts = contactRepository.findByEmail(email.getAddress());
            // check for duplicate email
            if (!contacts.isEmpty()) {
                if (contacts.size() == 1) {
                    Contact repoContact = contacts.get(0);
                    if (contact.getId() != repoContact.getId().longValue()) {
                        duplicate = true;
                    }
                } else {
                    duplicate = true;
                }

                if (duplicate) {
                    errors.rejectValue("email", "error.duplicate", new String[]{email.getAddress()}, null);
                    break;
                }
            }
        }
    }

    @Transactional(readOnly = false)
    @Override
    public Contact delete(Long id) {
        Contact contact = get(id);
        contactRepository.delete(contact);
        return contact;
    }

    @Override
    public void importContacts(InputStream inputStream, String originalFilename) {
        if (originalFilename.endsWith(".vcf")) {
            List<VCard> vcards = null;
            try {
                vcards = Ezvcard.parse(inputStream).all();
            } catch (IOException ex) {
                Logger.getLogger(ContactServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            int counter = 0;
            for (VCard vCard : vcards) {
                Contact contact = mapVCard2Contact(vCard);
                contactRepository.save(contact);
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private Contact mapVCard2Contact(VCard vCard) {
        Contact contact = contactRepository.createContact();

        // Name
        StructuredName structuredName = vCard.getStructuredName();
        if (structuredName != null) {
            contact.setFirstname(structuredName.getGiven());
            contact.setLastname(structuredName.getFamily());
        } else {
            FormattedName formattedName = vCard.getFormattedName();
            if (formattedName != null) {
                contact.setLastname(formattedName.getValue());
            }
        }
        if (contact.getLastname() == null) {
            contact.setLastname("?");
        }

        // Emails
        List<ezvcard.property.Email> emails = vCard.getEmails();
        for (ezvcard.property.Email ezEmail : emails) {
            Email email = contactRepository.createEmail();
            email.setAddress(ezEmail.getValue());

            final Set<ezvcard.parameter.EmailType> types = ezEmail.getTypes();
            for (ezvcard.parameter.EmailType ezEmailType : types) {
                email.setType(EmailType.HOME);
//              sb.append(emailType.getValue()).append(",");
            }

            contact.addEmail(email);
        }

        // Phones
        List<Telephone> phones = vCard.getTelephoneNumbers();
        for (Telephone ezPhone : phones) {
            Phone phone = contactRepository.createPhone();
            phone.setNumber(ezPhone.getText());

            final Set<ezvcard.parameter.TelephoneType> types = ezPhone.getTypes();
            for (ezvcard.parameter.TelephoneType ezPhoneType : types) {
                phone.setType(PhoneType.HOME);
//              sb.append(emailType.getValue()).append(",");
            }

            contact.addPhone(phone);
        }

        vCard.getAddresses();
        // Addresses
        List<ezvcard.property.Address> addresses = vCard.getAddresses();
        for (ezvcard.property.Address ezAddress : addresses) {
            Address address = contactRepository.createAddress();
            address.setStreet(ezAddress.getStreetAddress());
            address.setZipcode(ezAddress.getPostalCode());
            address.setCity(ezAddress.getLocality());

            final Set<ezvcard.parameter.AddressType> types = ezAddress.getTypes();
            for (ezvcard.parameter.AddressType ezAddressType : types) {
                address.setType(AddressType.HOME);
//              sb.append(emailType.getValue()).append(",");
            }

            contact.addAddress(address);
        }

        return contact;
    }

    @Override
    public DataSet<Contact> findByDatatablesCriterias(DatatablesCriterias criterias) {
        List<Contact> results = (List<Contact>) contactRepository.getFiltered(criterias);
        Long count = contactRepository.count();
        Long countFiltered = contactRepository.getFilteredCount(criterias);
        return new DataSet<>(results, count, countFiltered);
    }

    @Override
    public List<? extends Contact> findLimited(int maxResult) {
        return contactRepository.getLimited(maxResult);
    }

    @Override
    public Contact createContact() {
        return contactRepository.createContact();
    }
}
