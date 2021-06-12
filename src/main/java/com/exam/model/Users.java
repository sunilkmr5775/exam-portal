package com.exam.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
//import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER_DETAILS")
public class Users  implements UserDetails,Serializable 
{
	
	private static final long serialVersionUID = 1L;

	public Users() {
		 super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

//  @NotNull
	@Column(name = "USERNAME")
	private String username;

//	@JsonIgnore
	@Column(name = "PASSWORD")
	private String password;

//	@NotNull
	@Column(name = "FIRST_NAME")
	private String firstName;

//	@NotNull
	@Column(name = "LAST_NAME")
	private String lastName;

//	@Column(name = "GENDER")
//	private String gender;

//	@NotNull
	@Column(name = "EMAIL")
	private String email;

//	@NotNull
	@Column(name = "PHONE")
	private String phone;

	@Column(name = "ENABLED")
	private boolean enabled=true;

//	@OneToOne(cascade =  CascadeType.ALL, mappedBy = "userProfilePic")
//	private ProfilePic profilePic;

//	@Column(name = "PROFILE_IMAGE_ID")
//	private String profileImageId;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;
	
	private String status;
	private String errorCode;
	private String errorDescription;

//	User has many roles

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "users")
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="PROFILE_PIC_ID", referencedColumnName = "PROFILE_PIC_ID")
	private ProfilePic profilePic;
	
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}


	public ProfilePic getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(ProfilePic profilePic) {
		this.profilePic = profilePic;
	}

	
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<Authority> authoritySet = new HashSet<>();
		this.userRoles.forEach(userRole->{
			authoritySet.add(new Authority(userRole.getRole().getRoleName()));
		});
		return authoritySet;
	}



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
