/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

/**
 *
 * @author Android-3
 */
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private Long patentId;
    private boolean isLeaf;
    private String imgUrl;
    private String displayText;
    private String menuIcon;
    private String priceLimit;
    private String parentPath;
    private String loadCategory;

    /**
     * @return the categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the patentId
     */
    public Long getPatentId() {
        return patentId;
    }

    /**
     * @param patentId the patentId to set
     */
    public void setPatentId(Long patentId) {
        this.patentId = patentId;
    }

    /**
     * @return the isLeaf
     */
    public boolean isIsLeaf() {
        return isLeaf;
    }

    /**
     * @param isLeaf the isLeaf to set
     */
    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * @return the imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl the imgUrl to set
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the displayText
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * @param displayText the displayText to set
     */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getPriceLimit() {
		return priceLimit;
	}

	public void setPriceLimit(String priceLimit) {
		this.priceLimit = priceLimit;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getLoadCategory() {
		return loadCategory;
	}

	public void setLoadCategory(String loadCategory) {
		this.loadCategory = loadCategory;
	}
    
    
}
