/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Android-3
 */
@Entity
@Table(name = "review")
public class Review extends BaseDomain{

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
    
    @Basic(optional = false)
    @Column(name = "product_id")
    private long productId;
    
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "comment")
    private String comment;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "subject")
    private String subject;
    
    @Lob
    @Size(max = 655)
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "rating")
    private Double rating;
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
   
}
