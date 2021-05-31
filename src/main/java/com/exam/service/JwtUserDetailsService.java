package com.exam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.model.User;
//import com.myportal.model.UserDao;
//import com.myportal.model.UserDto;
import com.exam.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username in jwt service: "+username);
		User user = this.userRepository.findByUsername(username);
	
		if (user == null) {
			System.out.println("User not found");
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		System.out.println("username 3: "+user.getUsername());
		return (UserDetails) user;
	}

//	public User save(UserDto user) {
//		UserDao newUser = new UserDao();
//		newUser.setUsername(user.getUsername());
//		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
//		return userDao.save(newUser);
//	}
//	
//	public List<UserDao> findAll() {
//		List<UserDao> userList = new ArrayList<>();
//		userList = userDao.findAll();
////		for (UserDao user : userList) {
////			
////		}
//		return userList;
//	}
}