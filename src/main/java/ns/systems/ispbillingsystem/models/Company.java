/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name="companies")
@SQLDelete(sql = "UPDATE companies SET deleted_at = now() WHERE ID = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_at is null")
@NamedQuery(name = "Company.FindByName", query = "SELECT p FROM Company p WHERE p.name like :name")
public class Company {
    
   @Transient
   Logger logger = Logger.getLogger(this.getClass().getName());
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
    private Long id;
   
   @Column(name = "code" , nullable = false)
    private String code;
   
    @Column(name = "name" , nullable = false)
    private String name;
    
    @Column(name = "status", nullable = false, columnDefinition = "int default 1")
    private Integer status;
   
    @Column(name = "phone" , nullable = true)
    private String phone;
   
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
     
    
    @OneToMany(mappedBy = "company" ,cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages = new ArrayList<>();
    
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

    public Company() {
        super();
    }
  

    public Company(String code,String name, Integer status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }

    public Company(String code, String name, Integer status, String phone) {
        this.code = code;
        this.name = name;
        this.status = status;
        this.phone = phone;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    
    public String getStatusText(){
    return this.status==1?"Active":"In Active";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
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
