package com.exam.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.constant.StatusConstant;
import com.exam.exception.UserFoundException;
import com.exam.model.UserRole;
import com.exam.model.Users;
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
	public Users createUser(Users users, Set<UserRole> userRole) throws UserFoundException {
		
		Users local = this.userRepository.findByUsername(users.getUsername());
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		if(null!=local) {
			Users user1 = new Users();
			user1.setUsername(users.getUsername());
			user1.setStatus(StatusConstant.STATUS_FAILURE);
			user1.setErrorCode("102");
			user1.setErrorDescription("User already exist!!!");
			return user1;
//			System.out.println("User already exist!!!");
//			throw new UserFoundException("User already exist!!!","102");
		}else {
			// create user
			for(UserRole ur: userRole) {
				roleRepository.save(ur.getRole());
			}
			users.setStatus(StatusConstant.STATUS_SUCCESS);
			users.getUserRoles().addAll(userRole);
			users.setCreatedBy(users.getUsername());
			users.setCreatedDate(LocalDateTime.now());
//			user.setPassword(encoder.encode(user.getPassword()));
			users.setPassword(this.bcryptPassEncoder.encode(users.getPassword()));
			
			local = userRepository.save(users);
		}
		return local;
	}


	@Override
	public Users getUser(String username) {
		
		return this.userRepository.findByUsername(username);
	}


	@Override
	public void deleteUser(Long id) {
		
		this.userRepository.deleteById(id);
		
	}

}
