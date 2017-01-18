package com.weavers.duqhun.dao;

import com.weavers.duqhun.domain.BaseDomain;
import java.util.List;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;


/**
 *  Created by Suvankar Sen
 *	User: Suvankar Sen
 *  Company: CloudLabz
 *  Date: Aug 29, 2014
 *  Time: 12:15:06 PM
 *	Description:
 * @param <T>
 */
public interface BaseDao<T extends BaseDomain> {

    /**
     * Store an existing entity
     *
     * @param entity Object to store
     * @return Reference to the stored object.
     */
    T save(T entity);

    /**
     * Load the object belonging to the specified type
     *
     * @param entityId Long representing the type of the object to load
     * @return Object found belonging to the specified type
     * @throws org.springframework.orm.ObjectRetrievalFailureException if the entity for id does not exist
     */
    T loadById(Long entityId) throws ObjectRetrievalFailureException;

    /**
     * Returns  list of all objects
     * @return List of all available objects
     */
    List<T> loadAll();

    /**
     * Removes the provided item from the persistent storage
     *
     * @param entity Entity to remove from persistent storage
     */
    @Transactional
    void delete(T entity);
}
