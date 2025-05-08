package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Page<Reservation> findByUserIdOrderByReservationDateDesc(Integer userId, Pageable pageable);

	Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}

