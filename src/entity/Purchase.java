package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "purchases")
public class Purchase implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Flower flower;
    @ManyToOne
    private Buyer buyer;
    private int count;
    @Column
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    public Purchase() {
        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flower getFlower() {
        return flower;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.flower);
        hash = 97 * hash + Objects.hashCode(this.buyer);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Purchase other = (Purchase) obj;
        if (!Objects.equals(this.flower, other.flower)) {
            return false;
        }
        if (!Objects.equals(this.buyer, other.buyer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Purchase{" 
                + "flower=" + flower.getName()
                + ", buyer=" + buyer.getFirstname() + ", " + buyer.getLastname()
                + ", cost=" + flower.getCost()
                + ", count=" + getCount()
                + ", date=" + getPurchaseDate()
                + '}';
    }
}