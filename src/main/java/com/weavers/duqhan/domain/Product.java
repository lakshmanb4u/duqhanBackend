/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;


/**
 *
 * @author weaversAndroid
 */
@Entity
@Table(name = "product")
@Indexed
public class Product extends BaseDomain {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "name")
    @Field
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "category_id")
    private long categoryId;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    @Field
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "imgurl")
    private String imgurl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vendor_id")
    private Long vendorId;
    @Size(max = 20)
    @Column(name = "shipping_time")
    private String shippingTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "shipping_rate")
    private Double shippingRate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "parent_path")
    private String parentPath;
    @Lob
    @Size(max = 65535)
    @Column(name = "external_link")
    private String externalLink;
    @Lob
    @Size(max = 65535)
    @Column(name = "specifications")
    private String specifications;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "properties")
    private String properties;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_height")
    private double productHeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_length")
    private double productLength;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_weight")
    private double productWeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_width")
    private double productWidth;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productId", cascade = CascadeType.ALL)
    private List<ProductPropertiesMap> ProductPropertiesMaps;
    
    @Column(name = "like_unlike_count")
    private long likeUnlikeCount;
    
    @Column(name = "link_id")
    private long linkId;

    @Column(name = "thumb_img")
    private String thumbImg;

	public String getThumbImg() {
		 return thumbImg;
	 }
	
	public void setThumbImg(String thumbImg) {
		 this.thumbImg = thumbImg;
	 }
	 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Double getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(Double shippingRate) {
        this.shippingRate = shippingRate;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public double getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(double productHeight) {
        this.productHeight = productHeight;
    }

    public double getProductLength() {
        return productLength;
    }

    public void setProductLength(double productLength) {
        this.productLength = productLength;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(double productWidth) {
        this.productWidth = productWidth;
    }

    /**
     * @return the ProductPropertiesMaps
     */
    public List<ProductPropertiesMap> getProductPropertiesMaps() {
        return ProductPropertiesMaps;
    }

    /**
     * @param ProductPropertiesMaps the ProductPropertiesMaps to set
     */
    public void setProductPropertiesMaps(List<ProductPropertiesMap> ProductPropertiesMaps) {
        this.ProductPropertiesMaps = ProductPropertiesMaps;
    }

	public long getLikeUnlikeCount() {
		return likeUnlikeCount;
	}

	public void setLikeUnlikeCount(long likeUnlikeCount) {
		this.likeUnlikeCount = likeUnlikeCount;
	}

	public long getLinkId() {
		return linkId;
	}

	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}
	
	

}
