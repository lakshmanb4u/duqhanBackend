package com.weavers.duqhan.dao;

import java.util.List;

import com.weavers.duqhan.domain.RequestReturn;

public interface RequestReturnDao extends BaseDao<RequestReturn> {
	
	List<Object[]> getDetailByOrderId(Long orderId, int start, int limit);

}
