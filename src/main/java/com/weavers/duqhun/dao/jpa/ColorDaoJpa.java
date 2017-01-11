/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.ColorDao;
import com.weavers.duqhun.domain.Color;

/**
 *
 * @author Android-3
 */
public class ColorDaoJpa extends BaseDaoJpa<Color> implements ColorDao{
    
    public ColorDaoJpa() {
        super(Color.class, "Color");
    }
    
}
