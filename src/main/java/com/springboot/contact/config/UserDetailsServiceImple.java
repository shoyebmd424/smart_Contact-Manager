package com.springboot.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.contact.Entity.User;
import com.springboot.contact.dao.UserRepository;

public class UserDetailsServiceImple implements UserDetailsService {
	@Autowired
	private UserRepository userrepo; 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
	User user=	userrepo.findByEmail(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("user are not found ");
		}
		CustomUserDetail  customeUser=new CustomUserDetail(user);
		
		
		return customeUser;
	}

}
