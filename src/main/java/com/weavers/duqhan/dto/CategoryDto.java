/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;
import java.util.List;
/**
 *
 * @author Android-3
 */
public class CategoryDto {

    private Long categoryId;
    private String imgUrl;
    private String displayText;
    private String categoryName;
    private String menuIcon;
    /*private String menuIcon;
    private String priceLimit;
    private String parentPath;
    private String loadCategory;
    private Long patentId;
    private boolean isLeaf;
    */
    private List<CategoryDto> subCategories;

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

	public List<CategoryDto> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<CategoryDto> subCategories) {
		this.subCategories = subCategories;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
    
    
}
