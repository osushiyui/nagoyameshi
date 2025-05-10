package com.example.kadai_002.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.MembershipType;
import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.form.ReservationInputForm;
import com.example.kadai_002.repository.ReservationRepository;
import com.example.kadai_002.security.UserDetailsImpl;
import com.example.kadai_002.service.HouseService;
import com.example.kadai_002.service.ReservationService;
import com.example.kadai_002.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class ReservationController {
	private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final HouseService houseService;
    private final StripeService stripeService;
    
    public ReservationController(ReservationService reservationService,
    		ReservationRepository reservationRepository,
            HouseService houseService,
            StripeService stripeService) {
    	this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.houseService = houseService;
        this.stripeService = stripeService;
    }    
    
    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        
        return "reservations/index";
    }

    @GetMapping("/users/{userId}/reservations")
    public String showUserReservations(@PathVariable Integer userId, 
                                       @PageableDefault Pageable pageable,
                                       Model model) {
        Page<Reservation> reservations = reservationRepository.findByUserIdOrderByReservationDateDesc(userId, pageable);
        model.addAttribute("reservationPage", reservations);
        return "reservations/index";
    }
    
    @GetMapping("/houses/{houseId}/reservations/confirm")
    public String showEmptyReservationForm(@PathVariable Long houseId,
            @ModelAttribute("reservationInputForm") ReservationInputForm form,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            BindingResult bindingResult,
            Model model) {
    	
    	User user = userDetailsImpl.getUser();
        
        // ✅ 有料会員チェック
        if (!"PAID".equals(user.getMembershipType())) {
            return "redirect:/membership/upgrade";
        }

        House house = houseService.findById(houseId);
        form.setUserId(user.getId().intValue());
        
        if (form.getNumberOfPeople() != null && house.getPriceMin() != null) {
            BigDecimal peopleCount = new BigDecimal(form.getNumberOfPeople());
            BigDecimal priceMin = new BigDecimal(house.getPriceMin());
            form.setAmount(priceMin.multiply(peopleCount));
        }
       

        
        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", form);
        return "reservations/confirm";
    }

    @PostMapping("/houses/{houseId}/reservations/confirm")
    public String confirmReservation(@PathVariable Long houseId,
    		                         @ModelAttribute("reservationInputForm") @Valid ReservationInputForm form,
                                     BindingResult bindingResult,
                                     HttpServletRequest httpServletRequest,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                     Model model) {
    	 User user = userDetailsImpl.getUser();
    	    
    	    // ✅ 有料会員チェック
    	 if (user.getMembershipType() != MembershipType.PAID) {
    		    return "redirect:/membership/upgrade";
    		}
    	 
    	 if (user.isUserFree()) {
    		    return "redirect:/membership/upgrade";  // 無料会員ならサブスクリプション画面にリダイレクト
    		}



    	    House house = houseService.findById(houseId);
        
        if (form.getUserId() == null) {
            form.setUserId(user.getId().intValue());
        }
        
        form.setHouseId(houseId.intValue());
        
        if (bindingResult.hasErrors()) {            
            model.addAttribute("house", house);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "houses/show";
        }
        
        form.setHouseId(houseId.intValue());
        
        
        // 料金計算
        if (form.getNumberOfPeople() != null && house.getPriceMin() != null) {
            BigDecimal peopleCount = new BigDecimal(form.getNumberOfPeople());
            BigDecimal priceMin = new BigDecimal(house.getPriceMin());
            form.setAmount(priceMin.multiply(peopleCount));
        }
        
        
        
        String sessionId = stripeService.createStripeSession(house.getName(), form, httpServletRequest);
        
        
        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", form);
        model.addAttribute("userId", form.getUserId());
        model.addAttribute("sessionId", sessionId);
        return "reservations/confirm";
    }
    
    @PostMapping("/reservations/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationRepository.findById(id).ifPresent(r -> {
            r.setCanceled(true);
            reservationRepository.save(r);
        });
        return "redirect:/reservations";

    }

    
    
   /* @PostMapping("/houses/{houseId}/reservations/create")
    public String createReservation(@ModelAttribute("reservationInputForm") ReservationInputForm form) {
        reservationService.create(form);
        return "redirect:/users/" + form.getUserId() + "/reservations?reserved";
    }
*/

    }



