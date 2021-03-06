/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;

import com.weavers.duqhan.dao.CartDao;
import com.weavers.duqhan.domain.Cart;
import com.weavers.duqhan.domain.ProductSizeColorMap;
import java.util.List;
import javax.persistence.NoResultException;
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
        return query.getResultList();   // get productSizeColorMap for User
    }

    @Override
    public Cart loadByUserIdAndMapId(Long userId, Long sizecolormapId) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM Cart c WHERE c.sizecolormapId=:sizecolormapId AND c.userId=:userId");
            query.setParameter("userId", userId);
            query.setParameter("sizecolormapId", sizecolormapId);
            return (Cart) query.getSingleResult();  // // get cart for User by mapid
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Long getCartCountByUserId(Long userId) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(c) FROM Cart c WHERE c.userId=:userId"); // numner of products in a cart of a user
            query.setParameter("userId", userId);
            return (Long) query.getSingleResult();
        } catch (NoResultException nre) {
            return 0L;
        }
    }

    @Override
    public List<Cart> getCartByUserId(Long userId) {
        Query query = getEntityManager().createQuery("SELECT c FROM Cart c WHERE c.userId=:userId");    // all product of a cart for user 
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Cart getCartByIdAndMapId(Long cartId, Long mapId) {  //return cart for a purticular product.
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM Cart c WHERE c.id=:cartId AND c.sizecolormapId=:mapId");
            query.setParameter("cartId", cartId);
            query.setParameter("mapId", mapId);
            return (Cart) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
