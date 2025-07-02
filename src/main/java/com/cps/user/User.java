package com.cps.user;



import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String status;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "enabled")
 // ✅ New (null-safe)
    private Boolean enabled;
    
   

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public LocalDate getRegistrationDate() {
	    return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
	    this.registrationDate = registrationDate;
	}

	public LocalDate getExpiryDate() {
	    return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
	    this.expiryDate = expiryDate;
	}

    
	public boolean isEnabled() {
	    return Boolean.TRUE.equals(enabled); // Safe null check
	}

	    public void setEnabled(boolean enabled) {
	        this.enabled = enabled;
	    }
	    private String payment_status;

	  
		public String getPayment_status() {
			return payment_status;
		}
		public void setPayment_status(String payment_status) {
			this.payment_status = payment_status;
		}

	  
		@Column(unique = true)
		private String shortKey;



		public void setShortKey(String shortKey) {
			this.shortKey=shortKey;
			
		}
		public String getShortKey() {
			return shortKey;
		}


	
}