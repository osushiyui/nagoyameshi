package com.example.kadai_002.entity;



import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
        
    @Column(name = "furigana")
    private String furigana;    
        
    @Column(name = "postal_code")
    private String postalCode;
        
    @Column(name = "address")
    private String address;
        
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email")
    private String email;
        
    @Column(name = "password")
    private String password;  
    
    @Column(name = "stripe_customer_id")
    private String stripeCustomerId; // StripeCustomerIdフィールド追加
    
    @Enumerated(EnumType.STRING) 
    private MembershipType membershipType = MembershipType.FREE;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;   
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt; 
    
    
    public boolean isPaid() {
        return this.membershipType == MembershipType.PAID;
    }

    
    public boolean isUserPaid() {
        return this.membershipType == MembershipType.PAID;
    }
    
    public boolean isUserFree() {
        return this.membershipType == MembershipType.FREE;
    }



}
