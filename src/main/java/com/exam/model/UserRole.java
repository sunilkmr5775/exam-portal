package com.exam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ROLE_ID")
	private long userRoleId;

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne
	private Role role;

	public UserRole() {
	}

	public UserRole(long userRoleId, User user, Role role) {
		this.userRoleId = userRoleId;
		this.user = user;
		this.role = role;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
