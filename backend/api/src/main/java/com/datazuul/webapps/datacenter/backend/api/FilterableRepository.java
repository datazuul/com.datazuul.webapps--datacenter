package com.datazuul.webapps.datacenter.backend.api;

import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import java.util.List;

/**
 * Hint: do not use method names like "findByXYZ", gets in conflict with JPARepository
 * @author ralf
 * @param <T> the objects managed in the repository
 */
public interface FilterableRepository<T extends Object> {

    /**
     * @param maxResult Max number of results.
     * @return a maxResult limited list of results.
     */
    List<? extends T> getLimited(int maxResult);

    /**
     * Query used to populate the DataTables that display the list of results.
     *
     * @param criterias The DataTables criterias used to filter the results
     * (maxResult, filtering, paging, ...)
     * @return a filtered list of results.
     */
    List<? extends T> getFiltered(DatatablesCriterias criterias);

    /**
     * Query used to return the number of filtered results.
     *
     * @param criterias The DataTables criterias used to filter the results
     * (maxResult, filtering, paging, ...)
     * @return the number of filtered results.
     */
    Long getFilteredCount(DatatablesCriterias criterias);
}
