package com.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.exam.service.UserService;

@SpringBootApplication
public class ExamserverApplication 
//implements CommandLineRunner 
{

//	@Autowired
//	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private UserService userService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ExamserverApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("Starting code...");
//		
//		User user = new User();
//		user.setUserName("sunilkmr5775");
//		user.setPassword("sunilkmr123");
////		user.setPassword(bcryptEncoder.encode(user.getPassword()));
//		user.setFirstName("Sunil");
//		user.setLastName("Kumar");
//		user.setGender("M");
//		user.setPhone("8826653027");
//		user.setEmail("sunilkmr5775@gmail.com");
//		user.setStatus(true);
//		user.setAbout("I am passionate about learning new technologies");
//		user.setCreatedBy("sunilkmr5775");
//		user.setCreatedDate(LocalDateTime.now());
//		//user.set
//		
//		Role role1 = new Role();
//		role1.setRoleId(101);
//		role1.setRoleName("ADMIN");
//		role1.setCreatedBy("sunilkmr5775");
//		role1.setCreatedDate(LocalDateTime.now());
//		
//		Set<UserRole> userRoleSet = new HashSet<>();
//		UserRole userRole = new UserRole();
//		
//		userRole.setRole(role1);
//		userRole.setUser(user);
//		
//		userRoleSet.add(userRole);
//		
//		User user1 = this.userService.createUser(user, userRoleSet);
//		System.out.println("UserName ===> "+user1.getUserName());
//		
//	}

}
