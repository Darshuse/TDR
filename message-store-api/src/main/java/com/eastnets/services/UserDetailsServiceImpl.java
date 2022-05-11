package com.eastnets.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eastnets.entities.GrantedRoles;
import com.eastnets.entities.GrantedRolesId;
import com.eastnets.entities.User;
import com.eastnets.entities.UserPassword;
import com.eastnets.models.AuthenticationRequest;
import com.eastnets.repositories.UserRepository;
import com.eastnets.util.Constants;
import com.eastnets.util.HashingUtility;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger LOGGER = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GrantedRolesService grantedRolesService;

	public boolean isAuthenticated(AuthenticationRequest authenticationRequest) {

		String hashedPassword = null;
		boolean isAuthenticated = false;

		Optional<User> userOptional = userRepository.findUserByUsername(authenticationRequest.getUsername())
				.filter(userBean -> userBean.getAuthenticationmethod().equals("0"));

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			UserPassword userPassword = Collections.max(user.getDetails(),
					Comparator.comparing(bean -> bean.getPasswordId().getPasswordDate()));
			try {
				hashedPassword = HashingUtility.getHashedPassword(authenticationRequest.getPassword(),
						userPassword.getSalt().getBytes());

				if (hashedPassword.equals(userPassword.getPassword())) {

					GrantedRolesId id = new GrantedRolesId();
					id.setGroupId(user.getGroupId());
					id.setRoleName(Constants.USER_ROLE);
					Optional<GrantedRoles> grantedRoles = grantedRolesService.findGrantedRolesById(id);
					if (grantedRoles.isPresent()) {
						isAuthenticated = true;
						LOGGER.info("successfully logged in");
					} else {
						LOGGER.error("unauthorized User");
					}

				} else {
					LOGGER.error("incorrect Username or Password");
				}

			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}

		} else {
			LOGGER.error("incorrect Username or Password");
		}
		return isAuthenticated;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return new org.springframework.security.core.userdetails.User(userName, "", new ArrayList<>());
	}
}
