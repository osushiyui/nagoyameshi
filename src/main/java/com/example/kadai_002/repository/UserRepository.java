package com.example.kadai_002.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);


}
