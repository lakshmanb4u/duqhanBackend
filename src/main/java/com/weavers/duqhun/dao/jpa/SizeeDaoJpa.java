/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.SizeeDao;
import com.weavers.duqhun.domain.Sizee;

/**
 *
 * @author Android-3
 */
public class SizeeDaoJpa extends BaseDaoJpa<Sizee> implements SizeeDao{
    
    public SizeeDaoJpa() {
        super(Sizee.class, "Sizee");
    }
    
}
