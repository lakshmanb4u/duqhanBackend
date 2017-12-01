package com.weavers.duqhan.dto;

public class LikeUnlikeProductBean {
	
	private long userId;
	private long productId;
	private boolean likeUnlike;
	private String status;
	private String statusCode;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
