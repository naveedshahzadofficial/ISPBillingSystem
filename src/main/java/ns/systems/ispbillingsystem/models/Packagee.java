/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
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
@Table(name="packagees")
@SQLDelete(sql = "UPDATE packagees SET deleted_at = now() WHERE ID = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_at is null")
@NamedQuery(name = "Packagee.FindByName", query = "SELECT p FROM Packagee p WHERE p.name like :name")
public class Packagee  extends RecursiveTreeObject<Packagee>{
  
  @Transient 
  private Boolean isSelected = false;
  
   @Transient 
   private Integer quantity = 1;
 
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
    
    @Column(name = "price" , nullable = false)
    private Double price;
    
     @Column(name = "actual_price" , nullable = false)
    private Double actualPrice;
    
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
    
    @ManyToOne( fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "company_id")
    private Company company;
    
//    @ManyToMany(mappedBy = "packages")
//    private Set<Customer> customers = new HashSet<>();
   
    @OneToMany(mappedBy = "packagee",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CustomerPackage> customers = new HashSet<>();
    
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
    
    public Packagee() {
        super();
    }
    
    public Packagee(String code, String name, Double price, Double actualPrice, Integer status, Company company) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.actualPrice = actualPrice;
        this.status = status;
        this.company = company;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
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


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

//    public Set<Customer> getCustomers() {
//        return customers;
//    }
//
//    public void setCustomers(Set<Customer> customers) {
//        this.customers = customers;
//    }

    public Set<CustomerPackage> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerPackage> customers) {
        this.customers = customers;
    }


    @Override
    public String toString() {
        return this.name+" - Rs. "+this.getPriceFormat();
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getPriceFormat(){
        return ISPHelper.getPriceFormat(this.price);
    }

    
}
