package com.example.kadai_002.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }
}


