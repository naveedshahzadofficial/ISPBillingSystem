/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author naveed
 */
@Entity
@Table(name = "customer_packages")
public class CustomerPackage {

    @EmbeddedId
    private CustomerPackageId id = new CustomerPackageId();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("customerId")
    private Customer customer;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId("packageeId")
    private Packagee packagee;

    private int quantity;
    
    public CustomerPackage() {
    }

    public CustomerPackage(Customer customer, Packagee packagee, int quantity) {
        this.customer = customer;
        this.packagee = packagee;
        this.quantity = quantity;
    }

   
    
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Packagee getPackagee() {
        return packagee;
    }

    public void setPackagee(Packagee packagee) {
        this.packagee = packagee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CustomerPackage)) return false;
        CustomerPackage that = (CustomerPackage) obj;
        return Objects.equals(customer.getName(), that.customer.getName()) &&
                Objects.equals(packagee.getName(), that.packagee.getName()) &&
                Objects.equals(quantity, that.quantity);
    }
    
    @Override
    public int hashCode() {
               return Objects.hash(customer.getName(), packagee.getName(), quantity);
    }
    
    
    

    @Embeddable
    public static class CustomerPackageId implements Serializable {

        private static final long serialVersionUID = 1L;
        
        @Column(name = "customer_id")
        private Long customerId;
        
        @Column(name = "packagee_id")
        private Long packageeId;

        public CustomerPackageId() {
        }

        public CustomerPackageId(Long customerId, Long packageeId) {
            super();
            this.customerId = customerId;
            this.packageeId = packageeId;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public Long getPackageeId() {
            return packageeId;
        }

        public void setPackageeId(Long packageeId) {
            this.packageeId = packageeId;
        }
        
    }

}
