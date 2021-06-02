package com.exam.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.exam.model.UserRole;
import com.exam.model.Users;

@Service
public interface UserService {

	public Users createUser(Users users, Set<UserRole> userRole) throws Exception;
	
	public Users getUser(String username);
	
	public void deleteUser(Long id);
}
