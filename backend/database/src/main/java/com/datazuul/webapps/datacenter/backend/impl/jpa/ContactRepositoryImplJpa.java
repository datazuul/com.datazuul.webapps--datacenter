package com.datazuul.webapps.datacenter.backend.impl.jpa;

import com.datazuul.webapps.datacenter.backend.api.ContactRepository;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.AddressJpaEntity;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.ContactJpaEntity;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.EmailJpaEntity;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.PhoneJpaEntity;
import com.datazuul.webapps.datacenter.domain.contact.Address;
import com.datazuul.webapps.datacenter.domain.contact.Contact;
import com.datazuul.webapps.datacenter.domain.contact.Email;
import com.datazuul.webapps.datacenter.domain.contact.Phone;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.single-repository-behaviour
 *
 * @author ralf
 */
@Repository
public class ContactRepositoryImplJpa implements ContactRepository<ContactJpaEntity, Long> {

    @Autowired
    private ContactRepositoryJpa jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public Address createAddress() {
        return new AddressJpaEntity();
    }

    @Override
    public Contact createContact() {
        return new ContactJpaEntity();
    }

    @Override
    public Email createEmail() {
        return new EmailJpaEntity();
    }

    @Override
    public Phone createPhone() {
        return new PhoneJpaEntity();
    }

    @Override
    public void delete(Long id) {
        jpaRepository.delete(id);
    }

    @Override
    public void delete(ContactJpaEntity entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends ContactJpaEntity> entities) {
        jpaRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public boolean exists(Long id) {
        return jpaRepository.exists(id);
    }

    @Override
    public Iterable<ContactJpaEntity> findAll(Sort sort) {
        return jpaRepository.findAll(sort);
    }

    @Override
    public Page<ContactJpaEntity> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Iterable<ContactJpaEntity> findAll(Iterable<Long> ids) {
        return jpaRepository.findAll(ids);
    }

    @Override
    public Iterable<ContactJpaEntity> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<ContactJpaEntity> findByEmail(String address) {
        return jpaRepository.findByEmail(address);
    }

    @Override
    public ContactJpaEntity findOne(Long id) {
        return jpaRepository.findOne(id);
    }

    @Override
    public List<ContactJpaEntity> getFiltered(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM ContactJpaEntity c");
        /**
         * Step 1: global and individual column filtering
         */
        queryBuilder.append(getFilterQuery(criterias));
        /**
         * Step 2: sorting
         */
        if (criterias.hasOneSortedColumn()) {
            List<String> orderParams = new ArrayList<>();
            queryBuilder.append(" ORDER BY ");
            for (ColumnDef columnDef : criterias.getSortingColumnDefs()) {
                orderParams.add("c." + columnDef.getName() + " " + columnDef.getSortDirection());
            }
            Iterator<String> itr2 = orderParams.iterator();
            while (itr2.hasNext()) {
                queryBuilder.append(itr2.next());
                if (itr2.hasNext()) {
                    queryBuilder.append(" , ");
                }
            }
        }
        TypedQuery<ContactJpaEntity> query = entityManager.createQuery(queryBuilder.toString(), ContactJpaEntity.class);
        /**
         * Step 3: paging
         */
        query.setFirstResult(criterias.getDisplayStart());
        query.setMaxResults(criterias.getDisplaySize());
        return query.getResultList();
    }

    @Override
    public Long getFilteredCount(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM ContactJpaEntity c");
        queryBuilder.append(getFilterQuery(criterias));
        Query query = entityManager.createQuery(queryBuilder.toString());
        return Long.parseLong(String.valueOf(query.getResultList().size()));
    }

    @Override
    public List<ContactJpaEntity> getLimited(int maxResult) {
        TypedQuery<ContactJpaEntity> query = entityManager.createQuery("SELECT c FROM ContactJpaEntity c", ContactJpaEntity.class);
        query.setMaxResults(maxResult);
        final List<ContactJpaEntity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public <S extends ContactJpaEntity> S save(S entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public <S extends ContactJpaEntity> Iterable<S> save(Iterable<S> entities) {
        return jpaRepository.save(entities);
    }

    protected StringBuilder getFilterQuery(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder();
        List<String> paramList = new ArrayList<>();
        /**
         * Step 1.1: global filtering
         */
        if (StringUtils.isNotBlank(criterias.getSearch()) && criterias.hasOneFilterableColumn()) {
            queryBuilder.append(" WHERE ");
            for (ColumnDef columnDef : criterias.getColumnDefs()) {
                if (columnDef.isFilterable() && StringUtils.isBlank(columnDef.getSearch())) {
                    paramList.add(" LOWER(c." + columnDef.getName() + ") LIKE '%?%'".replace("?", criterias.getSearch().toLowerCase()));
                }
            }
            Iterator<String> itr = paramList.iterator();
            while (itr.hasNext()) {
                queryBuilder.append(itr.next());
                if (itr.hasNext()) {
                    queryBuilder.append(" OR ");
                }
            }
        }
        /**
         * Step 1.2: individual column filtering
         */
        if (criterias.hasOneFilterableColumn() && criterias.hasOneFilteredColumn()) {
            paramList = new ArrayList<>();
            if (!queryBuilder.toString().contains("WHERE")) {
                queryBuilder.append(" WHERE ");
            } else {
                queryBuilder.append(" AND ");
            }
            for (ColumnDef columnDef : criterias.getColumnDefs()) {
                if (columnDef.isFilterable()) {
                    if (StringUtils.isNotBlank(columnDef.getSearchFrom())) {
                        if (columnDef.getName().equalsIgnoreCase("birthDate")) {
                            paramList.add("c." + columnDef.getName() + " >= '" + columnDef.getSearchFrom() + "'");
                        } else {
                            paramList.add("c." + columnDef.getName() + " >= " + columnDef.getSearchFrom());
                        }
                    }
                    if (StringUtils.isNotBlank(columnDef.getSearchTo())) {
                        if (columnDef.getName().equalsIgnoreCase("birthDate")) {
                            paramList.add("c." + columnDef.getName() + " < '" + columnDef.getSearchTo() + "'");
                        } else {
                            paramList.add("c." + columnDef.getName() + " < " + columnDef.getSearchTo());
                        }
                    }
                    if (StringUtils.isNotBlank(columnDef.getSearch())) {
                        paramList.add(" LOWER(c." + columnDef.getName() + ") LIKE '%?%'".replace("?", columnDef.getSearch().toLowerCase()));
                    }
                }
            }
            Iterator<String> itr = paramList.iterator();
            while (itr.hasNext()) {
                queryBuilder.append(itr.next());
                if (itr.hasNext()) {
                    queryBuilder.append(" AND ");
                }
            }
        }
        return queryBuilder;
    }
}
