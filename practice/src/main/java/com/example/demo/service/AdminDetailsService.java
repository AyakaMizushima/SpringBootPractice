package com.example.demo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AdminSignup;
import com.example.demo.repository.AdminSignupRepository;

@Service
public class AdminDetailsService implements UserDetailsService {

	@Autowired
	private AdminSignupRepository adminRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		AdminSignup admin = adminRepo.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new User(admin.getEmail(), admin.getPassword(), Collections.emptyList());
	}
}