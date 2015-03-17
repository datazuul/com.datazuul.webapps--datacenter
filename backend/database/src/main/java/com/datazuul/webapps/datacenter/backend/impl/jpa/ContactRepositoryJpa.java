package com.datazuul.webapps.datacenter.backend.impl.jpa;

import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.ContactJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Contact repository
 *
 * @author ralf
 */
public interface ContactRepositoryJpa extends JpaRepository<ContactJpaEntity, Long> {

    public final static String FIND_BY_EMAILADDRESS_QUERY = "SELECT c FROM ContactJpaEntity c LEFT JOIN c.emails e WHERE e.address = :address";

    @Query(FIND_BY_EMAILADDRESS_QUERY)
    List<ContactJpaEntity> findByEmail(@Param("address") String emailAddress);
}
