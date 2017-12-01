package com.weavers.duqhan.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "like_unlike_product")
public class LikeUnlikeProduct extends BaseDomain {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
	
	@Basic(optional = false)
    @Column(name = "product_id")
    private long productId;
	
	@Basic(optional = false)
    @Column(name = "like_unlike")
    private boolean likeUnlike;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public boolean isLikeUnlike() {
		return likeUnlike;
	}

	public void setLikeUnlike(boolean likeUnlike) {
		this.likeUnlike = likeUnlike;
	}
	
	
}
