package com.weavers.duqhun.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * User: Anirban De Date: May 22, 2014 Time: 11:20:49 AM Base domain class for
 * all domain classes
 */
@MappedSuperclass
public class BaseDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    public BaseDomain() {
    }

    public BaseDomain(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings({"JpaModelErrorInspection"})
    public boolean isNew() {
        return (id == null);
    }
}
