package com.example.kadai_002.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.repository.ReservationRepository;

@Controller
public class ReservationController {
    private final ReservationRepository reservationRepository;      
    
    public ReservationController(ReservationRepository reservationRepository) {        
        this.reservationRepository = reservationRepository;              
    }    

    @GetMapping("/users/{userId}/reservations")
    public String showUserReservations(@PathVariable Integer userId, 
                                       @PageableDefault Pageable pageable,
                                       Model model) {
        Page<Reservation> reservations = reservationRepository.findByUserIdOrderByReservationDateDesc(userId, pageable);
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }
    
    @GetMapping("/houses/{houseId}/reservations/input")
    public String showReservationInput(@PathVariable Long houseId, Model model) {
        model.addAttribute("houseId", houseId); 
        return "reservations/confirm"; 


    }
}

