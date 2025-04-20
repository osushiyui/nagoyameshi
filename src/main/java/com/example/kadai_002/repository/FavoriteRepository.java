package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.User;

public interface FavoriteRepository extends JpaRepository <Favorite, Integer>{
	public Favorite findByHouseAndUser (House  house, User user);
	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);


}
