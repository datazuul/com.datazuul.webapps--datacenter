package com.datazuul.webapps.datacenter.frontend.webgui.controller;

import com.datazuul.webapps.datacenter.business.ContactService;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.EmailType;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for CRUD contacts.
 *
 * @author ralf
 */
@Controller
@RequestMapping("/contacts") // base path (under context "/contacts") for request mapping, "/" alone is not needed
// (To override a class-level mapping rather than refine it, place a slash in front of the method-level mapping.)
@SessionAttributes(value = {"contact"})
public class ContactsController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ContactService contactService;

    private String confirmationViewName;

    /**
     * Logical view name of the confirmation page that is shown after successful create.
     *
     * @param confirmationViewName logical view name of confirmation page
     */
    public void setConfirmationViewName(String confirmationViewName) {
        this.confirmationViewName = confirmationViewName;
    }

    @ModelAttribute("allEmailTypes")
    public List<EmailType> populateEmailTypes() {
        return Arrays.asList(EmailType.values());
    }

    @RequestMapping("home")
    // mapped to "/contacts/home[.*]"
    // combining the class-level "/" base path with the method-level "home" mapping
    public String homepage(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "contacts/contacts";
    }

    @RequestMapping(value = {"", "/", "list"})// mapped to "/contacts" and "/contacts/detail[.*]"
    // mapped to "/contacts/list[.*]" by convention (using method name),
    // e.g. "/contacts/list" or "/contacts/list.do" or "/contacts/list.html"
    public String list(Model model) {
//        Iterable<Contact> contacts = contactService.getAll();
        // Add the supplied attribute to this Map using a generated name: "contactList"
        // convention: Iterable/List<Contact> --> contactList; reference it with ${contactList}
//        model.addAttribute(contacts);
        // no logical view name defined, so it is "list" by convention
        return "contacts/list";
    }

    @RequestMapping(value = "all")
    @ResponseBody
    public Iterable<Contact> findAll(HttpServletRequest request) {
        return contactService.getAll();
    }

    @RequestMapping(value = "byCriterias")
    public @ResponseBody
    DatatablesResponse findAll(@DatatablesParams DatatablesCriterias criterias) {
        DataSet dataSet = contactService.findByDatatablesCriterias(criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("detail") // mapped to "/contacts/detail?id=..."
    // request param "id" will automatically parsed into Integer
    // <a th:href="@{detail(id=${contact.id})}"
    public String detail(@RequestParam("id") Long id, Model model, SessionStatus status) {
        Contact contact = contactService.get(id);
        model.addAttribute("contact", contact); // use explicit attribute name "contact", because contact is an instance of ContactJpaEntitiy put as "contactJpaEntity"...
        status.setComplete();
        // no logical view name defined, so it is "detail" by convention
        return "contacts/detail";
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET) // mapped to "/contacts/detail/{id}"
    // path variable "id" will automatically parsed into Integer
    // <a th:href="@{detail + '/' + ${contact.id}}"
    public String viewDetails(@PathVariable Long id, Model model, SessionStatus status) {
        Contact contact = contactService.get(id);
        model.addAttribute("contact", contact);
        status.setComplete();
        return "contacts/detail";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)// mapped to "/contacts/create[.*]"
    public String create(Model model) {
        // by returning it from the method it will be put on the Model under
        // the generated attribute name "contact"
        // return new Contact();
        model.addAttribute("contact", contactService.createContact());
        return "contacts/create";
    }

    @RequestMapping(value = "import", method = RequestMethod.GET)
    public String getImport(Model model) {
        return "contacts/import";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET) // mapped to "/contacts/edit/{id}"
    // path variable "id" will automatically parsed into Integer
    // <a th:href="@{edit + '/' + ${contact.id}}"
    public String edit(@PathVariable Long id, Model model) {
        Contact contact = contactService.get(id);
        model.addAttribute("contact", contact); // keep in session for the following post...
        return "contacts/create";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET) // mapped to "/contacts/detail/{id}"
    // path variable "id" will automatically parsed into Integer
    // <a th:href="@{detail + '/' + ${contact.id}}"
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Contact contact = contactService.delete(id);
        redirectAttributes.addFlashAttribute("infoMessage", messageSource.getMessage("info.contact_deleted",
                new Object[]{contact.getFirstname() + " " + contact.getLastname()}, Locale.GERMAN));
        return "redirect:/contacts/list";
    }

    /**
     * Whitelist of form backing bean fields. Fields that are not in this list will not be filled, but all other values
     * will!
     *
     * You can define explicit black/whitelists in your controllers. You can do this using so-called @InitBinder
     * methods. You’ll need one for each form. (If your controller has two forms, it needs two separate @InitBinder
     * methods with a specific value according to the name of the bean in the model.)
     *
     * @param binder form binder
     */
    @InitBinder("contact")
    public void initFormAddBinder(WebDataBinder binder) {
        binder.setAllowedFields(new String[]{"id", "firstname", "lastname", "emails*"});
    }

    /**
     * alternative form method:
     *
     * @param model Model of response
     */
//    @RequestMapping(method = RequestMethod.GET)
//    public void form(Model model) {
//        model.addAttribute("newContact", new Contact());
//    }
    /**
     * Handles form post request. see Contact from session:
     * http://stackoverflow.com/questions/24807427/new-modelattribute-is-getting-created-for-execution-of-each-requestmapping-metho
     *
     * @param contact form input (form backing bean)
     * @param model model
     * @param result
     * @return next logical view name
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)// mapped to "/contacts/create[.*]"
    public String create(@ModelAttribute("contact") @Valid Contact contact, BindingResult result, Model model, RedirectAttributes reAttr, SessionStatus status) {
        // By taking a Contact parameter, the submitted form data will be automatically
        // bound to a Contact bean, the bean will be placed on the model as an attribute
        // under its generated name ("contact"), and the bean will be passed into the
        // method itself. Using @ModelAttribute we choose to make it explicitely named "contact".
        // The BindingResult must come right after the model object that is validated or
        // else Spring will fail to validate the object and throw an exception.
        verifyBinding(result);
        if (result.hasErrors()) {
            return "contacts/create";
        }
        contactService.save(contact, result);
        if (result.hasErrors()) {
            return "contacts/create";
        }
//        model.addAttribute(contact); // replaced through @ModelAttribute

        // TODO locale
        reAttr.addFlashAttribute("infoMessage", messageSource.getMessage("info.contact_created", null, Locale.GERMAN));
        status.setComplete();
//        return confirmationViewName;
        return "redirect:/contacts/list";
// Any time the return type is a String,
        // DispatcherServlet assumes that the return value represents a logical view name.
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)// mapped to "/contacts/create[.*]"
    public String postEdit(@PathVariable Long id, @ModelAttribute("contact") @Valid Contact contact, BindingResult result, Model model, RedirectAttributes reAttr, SessionStatus status) {
        verifyBinding(result);
        if (result.hasErrors()) {
            return "contacts/create";
        }
        contact.setId(id);
        contactService.save(contact, result);
        if (result.hasErrors()) {
            return "contacts/create";
        }

        // TODO locale
        reAttr.addFlashAttribute("infoMessage", messageSource.getMessage("info.contact_created", null, Locale.GERMAN));
        status.setComplete();
        return "redirect:/contacts/list";
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String postImport(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes reAttr) {
        try {
            contactService.importContacts(file.getInputStream(), file.getOriginalFilename());
            // TODO locale
            reAttr.addFlashAttribute("infoMessage", messageSource.getMessage("info.contacts_imported", null, Locale.GERMAN));
        } catch (IOException ex) {
            Logger.getLogger(ContactsController.class.getName()).log(Level.SEVERE, null, ex);
            reAttr.addFlashAttribute("infoMessage", ex.getMessage());
        }

        return "redirect:/contacts/list";
    }

    private void verifyBinding(BindingResult br) {
        String[] suppressedFields = br.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Attempting to bind suppressed fields: "
                    + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
    }
}
