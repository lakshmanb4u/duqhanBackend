package com.weavers.duqhan.dto;

/**
 *
 * @author Android-3
 */
public class ProductNewBean {

    private Long productId;
    private String imgurl;
    private Double price;
    private Double discountedPrice;
    private Double discountPCT;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public Double getDiscountPCT() {
		return discountPCT;
	}
	public void setDiscountPCT(Double discountPCT) {
		this.discountPCT = discountPCT;
	}
    
    
}