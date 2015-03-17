package com.datazuul.webapps.datacenter.backend.impl.jpa;

import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.MusicTrackJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Music repository
 *
 * @author ralf
 */
public interface MusicRepositoryJpa extends JpaRepository<MusicTrackJpaEntity, Long> {
}
