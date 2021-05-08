/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

/**
 *
 * @author naveed
 */
@Entity
@Table(name="settings")
@SQLDelete(sql = "UPDATE settings SET deleted_at = now() WHERE ID = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_at is null")
@NamedQuery(name = "Setting.FindByName", query = "SELECT p FROM Setting p WHERE p.optionName like :optionName")
public class Setting {
    
    @Transient
    Logger logger = Logger.getLogger(this.getClass().getName());
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "option_name" , nullable = false)
    private String optionName;
    
    @Column(name = "option_value" , nullable = false, columnDefinition = "Text")
    private String optionValue;
    
    
    @Column(name= "option_status", columnDefinition = "tinyint(1) default true")
    private Boolean optionStatus = true;

   
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
   
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at", nullable = true, insertable = false)
    private Date deletedAt;
    
    @PrePersist 
    void prePersist() {
    if (this.createdAt == null)
       this.createdAt = new Date();
    if (this.updatedAt == null)
       this.updatedAt = new Date();
    }
   
    @PreUpdate
    public void preUpdate() {
        if (this.updatedAt == null)
        this.updatedAt = new Date();
    }

    public Setting() {
        super();
    }

    public Setting(String optionName, String optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getOptionStatus() {
        return optionStatus;
    }

    public void setOptionStatus(Boolean optionStatus) {
        this.optionStatus = optionStatus;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    
}
