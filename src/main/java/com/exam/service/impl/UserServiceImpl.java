package com.exam.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.exception.UserFoundException;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repository.RoleRepository;
import com.exam.repository.UserRepository;
import com.exam.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder bcryptPassEncoder;
	
	@Override
	public User createUser(User user, Set<UserRole> userRole) throws Exception {

		User local = this.userRepository.findByUsername(user.getUsername());
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		if(null!=local) {
			System.out.println("User already exist!!!");
			throw new UserFoundException("User already exist!!!","102");
		}else {
			// create user
			for(UserRole ur: userRole) {
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRole);
			user.setCreatedBy(user.getUsername());
			user.setCreatedDate(LocalDateTime.now());
//			user.setPassword(encoder.encode(user.getPassword()));
			user.setPassword(this.bcryptPassEncoder.encode(user.getPassword()));
			local = userRepository.save(user);
		}
		return local;
	}


	@Override
	public User getUser(String username) {
		
		return this.userRepository.findByUsername(username);
	}


	@Override
	public void deleteUser(Long id) {
		
		this.userRepository.deleteById(id);
		
	}

}
