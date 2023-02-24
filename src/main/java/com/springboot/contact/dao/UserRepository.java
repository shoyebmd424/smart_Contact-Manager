package com.springboot.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.contact.Entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
//	@Query("select u From User u WHERE u.email =:email")
//	public User getUserByUserName(@Param("email") String email);
	public User findByEmail(String email);
	public boolean existsByEmail(String email);
}
