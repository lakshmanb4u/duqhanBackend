/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.business;

import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.AouthBean;

/**
 *
 * @author Android-3
 */
public interface AouthService {

    AouthBean generatAccessToken(String email, Long userId, String codeName);

    String invalidatedToken(String email, String token);

    Users getUserByToken(String token);

    void updateEmailByUserId(Long userId, String newEmail);
}
