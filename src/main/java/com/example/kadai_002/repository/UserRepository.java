package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);
	 public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
	public Page<User> findByNameLikeOrFuriganaLikeOrEmailLike(String likeKeyword, String likeKeyword2,
			String likeKeyword3, Pageable pageable);
	}
