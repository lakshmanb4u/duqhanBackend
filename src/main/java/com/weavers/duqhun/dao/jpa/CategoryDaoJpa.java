/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.CategoryDao;
import com.weavers.duqhun.domain.Category;

/**
 *
 * @author Android-3
 */
public class CategoryDaoJpa extends BaseDaoJpa<Category> implements CategoryDao{
    
    public CategoryDaoJpa() {
        super(Category.class, "Category");
    }
    
}
