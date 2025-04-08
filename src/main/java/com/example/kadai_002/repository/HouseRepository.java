package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {
	public Page<House> findByNameLike(String keyword, Pageable pageable);

}
