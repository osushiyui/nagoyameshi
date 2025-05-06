package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	 public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    public List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);
    public Review findByHouseAndUser(House house, User user);
    public long countByHouse(House house);
    public Page<Review> findByHouseOrderByCreatedAtDesc(House house, Pageable pageable);
}

