/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.CategoryDao;
import com.weavers.duqhun.domain.Category;
import com.weavers.duqhun.dto.CategoryDto;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class CategoryDaoJpa extends BaseDaoJpa<Category> implements CategoryDao {

    public CategoryDaoJpa() {
        super(Category.class, "Category");
    }

    @Override
    public List<Category> getChildByParentId(Long parentId) {
        Query query = getEntityManager().createQuery("SELECT c FROM Category c WHERE c.parentId=:parentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

}
