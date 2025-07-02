package com.cps.profile;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String profilePicUrl;
    private String dob;
    private String qualification;
    private String designation;

    private String currentCompany;
    private String companyDesignation;
    private String companyLogoUrl;
    private String companyContact;
    private String companyEmail;
    private String companyAddress;
    private String companyLocation;

    private String primaryNumber;
    private String alternateNumber;

    private String link1;
    private String link2;
    private String link3;
    private String link4;
    private String link5;
    private String link6;
    private String link7;
    private String link8;
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
	public String getProfilePicUrl() {
		return profilePicUrl;
	}
	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCompanyName() {
		return currentCompany;
	}
	public void setCompanyName(String currentCompany) {
		this.currentCompany = currentCompany;
	}
	public String getCompanyDesignation() {
		return companyDesignation;
	}
	public void setCompanyDesignation(String companyDesignation) {
		this.companyDesignation = companyDesignation;
	}
	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}
	public void setCompanyLogoUrl(String companyLogoUrl) {
		this.companyLogoUrl = companyLogoUrl;
	}
	public String getCompanyPhone() {
		return companyContact;
	}
	public void setCompanyPhone(String companyContact) {
		this.companyContact = companyContact;
	}
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyLocation() {
		return companyLocation;
	}
	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}
	public String getPrimaryPhone() {
		return primaryNumber;
	}
	public void setPrimaryPhone(String primaryNumber) {
		this.primaryNumber = primaryNumber;
	}
	public String getAlternatePhone() {
		return alternateNumber;
	}
	public void setAlternatePhone(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}
	public String getLink1() {
		return link1;
	}
	public void setLink1(String link1) {
		this.link1 = link1;
	}
	public String getLink2() {
		return link2;
	}
	public void setLink2(String link2) {
		this.link2 = link2;
	}
	public String getLink3() {
		return link3;
	}
	public void setLink3(String link3) {
		this.link3 = link3;
	}
	public String getLink4() {
		return link4;
	}
	public void setLink4(String link4) {
		this.link4 = link4;
	}
	public String getLink5() {
		return link5;
	}
	public void setLink5(String link5) {
		this.link5 = link5;
	}
	public String getLink6() {
		return link6;
	}
	public void setLink6(String link6) {
		this.link6 = link6;
	}
	public String getLink7() {
		return link7;
	}
	public void setLink7(String link7) {
		this.link7 = link7;
	}
	public String getLink8() {
		return link8;
	}
	public void setLink8(String link8) {
		this.link8 = link8;
	}

    // Add Getters and Setters for all fields (same as your version)
}
