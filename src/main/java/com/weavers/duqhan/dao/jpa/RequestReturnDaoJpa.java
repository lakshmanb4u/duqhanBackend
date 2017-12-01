package com.weavers.duqhan.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import com.weavers.duqhan.dao.RequestReturnDao;
import com.weavers.duqhan.domain.RequestReturn;
import com.weavers.duqhan.util.StatusConstants;

public class RequestReturnDaoJpa extends BaseDaoJpa<RequestReturn> implements RequestReturnDao {

	RequestReturnDaoJpa() {
		super(RequestReturn.class, "RequestReturn");
	}
	
	@Override
    public List<Object[]> getDetailByOrderId(Long orderId, int start, int limit) {
        Query query = getEntityManager().createQuery("select * from request_return where order_id=:orderId").setFirstResult(start).setMaxResults(limit);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
