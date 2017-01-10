/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.CartDao;
import com.weavers.duqhun.domain.Cart;

/**
 *
 * @author Android-3
 */
public class CartDaoJpa extends BaseDaoJpa<Cart> implements CartDao{
    
    public CartDaoJpa() {
        super(Cart.class, "Cart");
    }
    
}
