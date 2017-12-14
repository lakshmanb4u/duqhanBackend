package com.weavers.duqhan.dto;
import com.weavers.duqhan.domain.LikeUnlikeProduct;

public class LikeDto {
	private LikeUnlikeProduct likeUnlikeDetails;
	private String statusCode;
    private String status;

	public LikeUnlikeProduct getLikeUnlikeDetails() {
		return likeUnlikeDetails;
	}

	public void setLikeUnlikeDetails(LikeUnlikeProduct likeUnlikeDetails) {
		this.likeUnlikeDetails = likeUnlikeDetails;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}