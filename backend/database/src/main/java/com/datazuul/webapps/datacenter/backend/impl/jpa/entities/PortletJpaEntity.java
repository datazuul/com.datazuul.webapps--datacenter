package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

import com.datazuul.webapps.datacenter.domain.Portlet;
import com.datazuul.webapps.datacenter.domain.Configuration;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author ralf
 */
public class PortletJpaEntity implements Portlet {
    /*


     You can also map directly to a table without creating a KeyValuePair class

     For a map property with the key-value pairs stored in MY_MAP_TABLE and defined as a property named 'settings':

     Define the property:

     @ElementCollection (fetch=FetchType.EAGER)
     @CollectionTable(name="MY_MAP_TABLE" , joinColumns=@JoinColumn(name="ID"))
     @MapKeyColumn(name="name")
     @Column(name="value")
     public Map<String, String> getSettings() {
     return settings;
     }

     And the table to store the map:

     CREATE TABLE MY_MAP_TABLE (
     ID          NUMBER not null REFERENCES  MY_PARENT_TABLE(ID),
     NAME        VARCHAR2(256) not null,
     VALUE       VARCHAR2(256) not null,
     PRIMARY KEY (ID , NAME)
     );


     */
//    @ElementCollection
//    @CollectionTable(name = "Nicknames", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "nickname")
//    public List<String> getNicknames() {
//    }
// @Table(name = "Employee", uniqueConstraints = {
//@UniqueConstraint(columnNames = "ID"),
//@UniqueConstraint(columnNames = "EMAIL") })
//public class EmployeeEntity implements Serializable {
//    @OneToMany(cascade=CascadeType.ALL)
//    @JoinColumn(name="EMPLOYEE_ID")
//    private Set<AccountEntity> accounts;
    // getList: java/util/List.html#addAll(java.util.Collection)
    // http://howtodoinjava.com/2012/11/17/hibernate-one-to-many-mapping-using-annotations/

    @Override
    public Configuration getSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
