/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.business;

import com.weavers.duqhun.dto.AouthBean;

/**
 *
 * @author Android-3
 */
public interface AouthService {
    
    AouthBean generatAccessToken(String email, Long userId);
    
}
