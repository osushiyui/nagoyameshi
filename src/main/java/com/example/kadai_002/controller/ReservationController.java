package com.example.kadai_002.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.form.ReservationInputForm;
import com.example.kadai_002.repository.ReservationRepository;
import com.example.kadai_002.service.HouseService;
import com.example.kadai_002.service.ReservationService;

import jakarta.validation.Valid;

@Controller
public class ReservationController {
	private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final HouseService houseService;
    
    public ReservationController(ReservationService reservationService,
    		ReservationRepository reservationRepository,
            HouseService houseService) {
    	this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.houseService = houseService;
    }    

    @GetMapping("/users/{userId}/reservations")
    public String showUserReservations(@PathVariable Integer userId, 
                                       @PageableDefault Pageable pageable,
                                       Model model) {
        Page<Reservation> reservations = reservationRepository.findByUserIdOrderByReservationDateDesc(userId, pageable);
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }
    
    @GetMapping("/houses/{houseId}/reservations/confirm")
    public String showEmptyReservationForm(@PathVariable Long houseId,
            @ModelAttribute("reservationInputForm") @Valid ReservationInputForm form,
            BindingResult bindingResult,
            Model model) {
        House house = houseService.findById(houseId);
        
        if (form.getNumberOfPeople() != null && house.getPriceMin() != null) {
            BigDecimal peopleCount = new BigDecimal(form.getNumberOfPeople());
            BigDecimal priceMin = new BigDecimal(house.getPriceMin());
            form.setAmount(priceMin.multiply(peopleCount));
        }
       
        
        if (bindingResult.hasErrors()) {            
            model.addAttribute("house", house);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "houses/show";
        }

        
        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", form);
        return "reservations/confirm";
    }

    @PostMapping("/houses/{houseId}/reservations/confirm")
    public String confirmReservation(@PathVariable Long houseId,
                                     @ModelAttribute ReservationInputForm form,
                                     Model model) {
        House house = houseService.findById(houseId);
        
        // 料金計算
        if (form.getNumberOfPeople() != null && house.getPriceMin() != null) {
            BigDecimal peopleCount = new BigDecimal(form.getNumberOfPeople());
            BigDecimal priceMin = new BigDecimal(house.getPriceMin());
            form.setAmount(priceMin.multiply(peopleCount));
        }
        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", form);
        model.addAttribute("userId", form.getUserId());
        return "reservations/confirm";
    }
    
    @PostMapping("/houses/{houseId}/reservations/create")
    public String createReservation(@ModelAttribute("reservationInputForm") ReservationInputForm form) {
        reservationService.create(form);
        return "redirect:/users/" + form.getUserId() + "/reservations";
    }



}

