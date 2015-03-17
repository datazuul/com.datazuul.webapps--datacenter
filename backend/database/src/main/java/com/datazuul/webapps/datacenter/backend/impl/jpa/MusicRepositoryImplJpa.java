package com.datazuul.webapps.datacenter.backend.impl.jpa;

import com.datazuul.webapps.datacenter.backend.api.MusicRepository;
import com.datazuul.webapps.datacenter.backend.impl.jpa.entities.MusicTrackJpaEntity;
import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
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
 *
 * @author ralf
 */
@Repository
public class MusicRepositoryImplJpa implements MusicRepository<MusicTrackJpaEntity, Long> {

    @Autowired
    private MusicRepositoryJpa jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public MusicTrack createMusicTrack() {
        return new MusicTrackJpaEntity();
    }

    @Override
    public void delete(Long id) {
        jpaRepository.delete(id);
    }

    @Override
    public void delete(MusicTrackJpaEntity entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends MusicTrackJpaEntity> entities) {
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
    public Iterable<MusicTrackJpaEntity> findAll(Sort sort) {
        return jpaRepository.findAll(sort);
    }

    @Override
    public Page<MusicTrackJpaEntity> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Iterable<MusicTrackJpaEntity> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Iterable<MusicTrackJpaEntity> findAll(Iterable<Long> ids) {
        return jpaRepository.findAll(ids);
    }

    @Override
    public MusicTrackJpaEntity findOne(Long id) {
        return jpaRepository.findOne(id);
    }

    @Override
    public List<? extends MusicTrackJpaEntity> getFiltered(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder("SELECT m FROM MusicTrackJpaEntity m");
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
                orderParams.add("m." + columnDef.getName() + " " + columnDef.getSortDirection());
            }
            Iterator<String> itr2 = orderParams.iterator();
            while (itr2.hasNext()) {
                queryBuilder.append(itr2.next());
                if (itr2.hasNext()) {
                    queryBuilder.append(" , ");
                }
            }
        }
        TypedQuery<MusicTrackJpaEntity> query = entityManager.createQuery(queryBuilder.toString(), MusicTrackJpaEntity.class);
        /**
         * Step 3: paging
         */
        query.setFirstResult(criterias.getDisplayStart());
        query.setMaxResults(criterias.getDisplaySize());
        return query.getResultList();
    }

    @Override
    public Long getFilteredCount(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder("SELECT m FROM MusicTrackJpaEntity m");
        queryBuilder.append(getFilterQuery(criterias));
        Query query = entityManager.createQuery(queryBuilder.toString());
        return Long.parseLong(String.valueOf(query.getResultList().size()));
    }

    @Override
    public List<? extends MusicTrackJpaEntity> getLimited(int maxResult) {
        TypedQuery<MusicTrackJpaEntity> query = entityManager.createQuery("SELECT m FROM MusicTrackJpaEntity m", MusicTrackJpaEntity.class);
        query.setMaxResults(maxResult);
        final List<MusicTrackJpaEntity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public <S extends MusicTrackJpaEntity> S save(S entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public <S extends MusicTrackJpaEntity> Iterable<S> save(Iterable<S> entities) {
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
                    paramList.add(" LOWER(m." + columnDef.getName() + ") LIKE '%?%'".replace("?", criterias.getSearch().toLowerCase()));
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
                            paramList.add("m." + columnDef.getName() + " >= '" + columnDef.getSearchFrom() + "'");
                        } else {
                            paramList.add("m." + columnDef.getName() + " >= " + columnDef.getSearchFrom());
                        }
                    }
                    if (StringUtils.isNotBlank(columnDef.getSearchTo())) {
                        if (columnDef.getName().equalsIgnoreCase("birthDate")) {
                            paramList.add("m." + columnDef.getName() + " < '" + columnDef.getSearchTo() + "'");
                        } else {
                            paramList.add("m." + columnDef.getName() + " < " + columnDef.getSearchTo());
                        }
                    }
                    if (StringUtils.isNotBlank(columnDef.getSearch())) {
                        paramList.add(" LOWER(m." + columnDef.getName() + ") LIKE '%?%'".replace("?", columnDef.getSearch().toLowerCase()));
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
