/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.OtpTableDao;
import com.weavers.duqhun.domain.OtpTable;

/**
 *
 * @author Android-3
 */
public class OtpTableDaoJpa extends BaseDaoJpa<OtpTable> implements OtpTableDao {
    
    public OtpTableDaoJpa() {
        super(OtpTable.class, "OtpTable");
    }
    
}
