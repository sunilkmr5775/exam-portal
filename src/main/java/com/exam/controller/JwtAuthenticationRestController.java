package com.exam.controller;

import java.util.Objects;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.exam.exception.UserNotFoundException;
import com.exam.jwt.JwtTokenUtil;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.service.JwtUserDetailsService;

@RestController
@Component
@CrossOrigin("*")
public class JwtAuthenticationRestController {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

//	@Autowired
//	private UserDetailsService userjwtDetailsService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value ="${jwt.get.token.uri}", method = RequestMethod.POST)
	public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		JwtResponse jwtResponse = new JwtResponse();
		
		try{
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		}catch(UserNotFoundException ex) {
			ex.printStackTrace();
			jwtResponse.setStatus("FAILURE");
			jwtResponse.setErrorCode("100");
			jwtResponse.setErrorDesc("USER NOT FOUND");
			throw new Exception("User not found !!!");
		}

		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = this.jwtTokenUtil.generateToken(userDetails);
		 jwtResponse.setStatus("SUCCESS");
		 jwtResponse.setJwttoken(token);
//		return ResponseEntity.ok(new JwtResponse(token));
		 return jwtResponse;
	}



//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
//		return ResponseEntity.ok(userDetailsService.save(user));
//	}

//	@RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
//	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
//		String authToken = request.getHeader(tokenHeader);
//		final String token = authToken.substring(7);
//		String username = jwtTokenUtil.getUsernameFromToken(token);
//		User user = (User) userDetailsService.loadUserByUsername(username);
//
//		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
//			String refreshedToken = jwtTokenUtil.refreshToken(token);
//			return ResponseEntity.ok(new JwtResponse(refreshedToken));
//		} else {
//			return ResponseEntity.badRequest().body(null);
//		}
//	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	
	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED: " + e.getMessage());
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS: " + e.getMessage());
		}
	}
	
	
}
