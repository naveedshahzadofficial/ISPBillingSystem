/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name="customers")
@SQLDelete(sql = "UPDATE customers SET deleted_at = now() WHERE ID = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_at is null")
@NamedQuery(name = "Customer.FindByName", query = "SELECT p FROM Customer p WHERE p.name like :name")
public class Customer {
    
    @Transient
   Logger logger = Logger.getLogger(this.getClass().getName());
    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false ,nullable = false)
    private Long id;
    
    @Column(name = "code" , nullable = false)
    private String code;
    
    @Column(name = "name" , nullable = false)
    private String name;
    
    @Column(name = "discount" , nullable = true)
    private Double discount;
    
    @Column(name = "phone" , nullable = false)
    private String phone;
    
    @Column(name = "cnic" , nullable = true)
    private String cnic;
    
    @Column(name = "address" , nullable = true, columnDefinition = "text")
    private String address;
   
    @Column(name = "status", nullable = false, columnDefinition = "int default 1")
    private Integer status;
   
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
    
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "customer_packages", 
        joinColumns = { @JoinColumn(name = "customer_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "package_id"),
        }
    )
    Set<Package> packages = new HashSet<>();
    
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

    public Customer() {
        super();
    }

    public Customer(String code, String name, Double discount, String phone, String cnic, String address, Integer status) {
        this.code = code;
        this.name = name;
        this.discount = discount;
        this.phone = phone;
        this.cnic = cnic;
        this.address = address;
        this.status = status;
    }
    
    

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Package> getPackages() {
        return packages;
    }

    public void setPackages(Set<Package> packages) {
        this.packages = packages;
    }
    
    
    
    
}
