package com.exam.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.exception.UserFoundException;
import com.exam.exception.UserNotFoundException;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.service.JwtUserDetailsService;
import com.exam.service.UserService;

@RestController
@Component
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	// Returns the details of current user
	@GetMapping(value = "/current-user")
	public User getCurrentUser(Principal principal) {
		System.out.println("username in controller: "+principal.getName());
		return ((User) this.userDetailsService.loadUserByUsername(principal.getName()));
	}
	
	
	@PostMapping("/")
	public User createUser(@RequestBody User user) throws Exception {
		
		user.setProfileImage(user.getFirstName()+".png");
		
		Role role = new Role();
		role.setRoleId(101);
		role.setRoleName("ADMIN");
		role.setCreatedBy(user.getUsername());
		role.setCreatedDate(LocalDateTime.now());
		
		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRole.setUser(user);
		
		Set<UserRole> userRoleSet = new HashSet<>();
		userRoleSet.add(userRole);
		
		return this.userService.createUser(user, userRoleSet);
	}
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username") String username) {
		return this.userService.getUser(username);
		
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		 this.userService.deleteUser(userId);
		
	}
	
//	update API 
	
//	@ExceptionHandler(UserFoundException.class)
//	public ResponseEntity<?> exceptionalHandler(UserFoundException ex){
//		return ResponseEntity.ok(new UserFoundException());
//		
//	}
	
	
}
