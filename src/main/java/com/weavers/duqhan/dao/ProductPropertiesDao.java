/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao;

import com.weavers.duqhan.domain.ProductProperties;
import java.util.List;

/**
 *
 * @author weaversAndroid
 */
public interface ProductPropertiesDao extends BaseDao<ProductProperties> {

    ProductProperties loadByName(String propertyName);

    List<ProductProperties> loadByIds(List<Long> properyIds);
}
