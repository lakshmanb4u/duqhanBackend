/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.CategoryDao;
import com.weavers.duqhan.domain.Category;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
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
    public List<Category> loadByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        String q = "SELECT c FROM Category AS c WHERE c.id IN (";
        int i = 0;
        String s = "";
        for (Long categoryId : ids) {
            s = s + (i == 0 ? "" : ",") + ":id" + i++;
        }
        Query query = getEntityManager().createQuery(q + s + ")");
        i = 0;
        for (Long categoryId : ids) {
            query.setParameter("id" + i++, categoryId);
        }
        return query.getResultList();
    }

    @Override
    public List<Category> getChildByParentId(Long parentId) {
        Query query = getEntityManager().createQuery("SELECT c FROM Category c WHERE c.parentId=:parentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

    @Override
    public Category getCategoryByName(String name) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM Category c WHERE c.name=:name");
            query.setParameter("name", name);
            return (Category) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
