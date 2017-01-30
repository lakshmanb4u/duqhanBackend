/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao;

import com.weavers.duqhun.domain.OrderDetails;
import java.util.List;

/**
 *
 * @author Android-3
 */
public interface OrderDetailsDao extends BaseDao<OrderDetails>{
    
    List<OrderDetails> getDetailBypaymentIdAndUserId(String paymentId, Long userId);
    
    List<Object[]> getDetailByUserId(Long userId);
    
}
