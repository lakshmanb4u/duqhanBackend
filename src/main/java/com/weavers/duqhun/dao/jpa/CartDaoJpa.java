/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.CartDao;
import com.weavers.duqhun.domain.Cart;
import com.weavers.duqhun.domain.ProductSizeColorMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class CartDaoJpa extends BaseDaoJpa<Cart> implements CartDao {

    public CartDaoJpa() {
        super(Cart.class, "Cart");
    }

    @Override
    public List<ProductSizeColorMap> getProductSizeColorMapByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT map FROM ProductSizeColorMap map, Cart c WHERE c.sizecolormapId=map.id AND c.userId=:userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
