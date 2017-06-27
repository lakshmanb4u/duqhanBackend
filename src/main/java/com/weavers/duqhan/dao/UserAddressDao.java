/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.UserAddress;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface UserAddressDao extends BaseDao<UserAddress> {

    UserAddress getAddressByIdAndUserId(Long id, Long userId);
    
    List<UserAddress> getAddressByUserId(Long userId);
    
    List<UserAddress> getActiveAddressByUserId(Long userId);
    
    UserAddress getDefaultAddressByUserId(Long userId);
}
