/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dao.jpa;
import com.weavers.duqhan.dao.RecordedActionsDao;
import com.weavers.duqhan.domain.RecordedActions;

/**
 *
 * @author Android-3
 */
public class RecordedActionsDaoJap extends BaseDaoJpa<RecordedActions> implements RecordedActionsDao {
	
	public RecordedActionsDaoJap() {
        super(RecordedActions.class, "Cart");
    }
}