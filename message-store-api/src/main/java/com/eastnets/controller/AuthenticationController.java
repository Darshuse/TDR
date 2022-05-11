package com.eastnets.controller;

import java.text.SimpleDateFormat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eastnets.models.AuthenticationRequest;
import com.eastnets.models.AuthenticationResponse;
import com.eastnets.services.UserDetailsServiceImpl;
import com.eastnets.util.JwtUtil;

@RestController
@RequestMapping("/messageStoreAPI")
public class AuthenticationController {
	private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@Autowired
	private UserDetailsServiceImpl userService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/authentication", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		LOGGER.info("checking User authentication ...");

		Boolean isAuthenticated = userService.isAuthenticated(authenticationRequest);

		AuthenticationResponse authenticationResponse = null;
		if (isAuthenticated) {

			final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

			final String jwt = jwtUtil.generateToken(userDetails);
			String expirationDate = formatter.format(jwtUtil.extractExpiration(jwt));
			LOGGER.debug("generated Token will be expired after " + expirationDate);

			authenticationResponse = new AuthenticationResponse(jwt, expirationDate, HttpStatus.CREATED);
			return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
		} else {
			authenticationResponse = new AuthenticationResponse("", "", HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(authenticationResponse, HttpStatus.UNAUTHORIZED);
		}
	}

}
