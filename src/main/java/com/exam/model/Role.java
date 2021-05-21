package com.exam.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ROLE")
public class Role {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ROLE_ID")
	private long roleId;

//  @NotNull
	@Column(name = "ROLE_NAME")
	private String roleName;
	
//	User has many roles

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "role")
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	
	
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;



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

	public Role() {
		super();
	}

	public Role(long roleId, String roleName, String createdBy, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	
	

}
