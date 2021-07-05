/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.models;

import java.io.Serializable;
import java.util.Date;
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
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author naveed
 */
@Entity
@Table(name = "invoice_packages")
@SQLDelete(sql = "UPDATE invoice_packages SET deleted_at = now() WHERE ID = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_at is null")
public class InvoicePackage {
    
    @EmbeddedId
    private InvoicePackageId id = new InvoicePackageId();
   
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("invoiceId")
    private Invoice invoice;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId("packageeId")
    private Packagee packagee;
    
    @Column(name = "package_name" , nullable = false)
    private String packageName;
    
    @Column(name = "quantity" , nullable = false)
    private int quantity;
    
    @Column(name = "price" , nullable = false)
    private Double price;
    
    @Column(name = "actual_price" , nullable = false)
    private Double actualPrice;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at", nullable = true, insertable = false)
    private Date deletedAt;

    public InvoicePackage() {
    }

    public InvoicePackage(Invoice invoice, Packagee packagee, String packageName, int quantity, Double price, Double actualPrice) {
        this.invoice = invoice;
        this.packagee = packagee;
        this.packageName = packageName;
        this.quantity = quantity;
        this.price = price;
        this.actualPrice = actualPrice;
    }

    public InvoicePackageId getId() {
        return id;
    }

    public void setId(InvoicePackageId id) {
        this.id = id;
    }
    
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Packagee getPackagee() {
        return packagee;
    }

    public void setPackagee(Packagee packagee) {
        this.packagee = packagee;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    
    
    @Embeddable
    public static class InvoicePackageId implements Serializable {

        private static final long serialVersionUID = 1L;
        
        @Column(name = "invoice_id")
        private Long invoiceId;
        
        @Column(name = "packagee_id")
        private Long packageeId;

        public InvoicePackageId() {
        }

        public Long getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
        }

        public Long getPackageeId() {
            return packageeId;
        }

        public void setPackageeId(Long packageeId) {
            this.packageeId = packageeId;
        }
        
        
    }
    
}
