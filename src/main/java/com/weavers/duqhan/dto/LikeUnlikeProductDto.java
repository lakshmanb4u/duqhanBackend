package com.weavers.duqhan.dto;

public class LikeUnlikeProductDto {
	
	private long userId;
	private long productId;
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
