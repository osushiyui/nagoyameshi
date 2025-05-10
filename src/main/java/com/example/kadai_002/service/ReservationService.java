package com.example.kadai_002.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.HouseRepository;
import com.example.kadai_002.repository.ReservationRepository;
import com.example.kadai_002.repository.UserRepository;

@Service
public class ReservationService {
	
    private final ReservationRepository reservationRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
            HouseRepository houseRepository,
            UserRepository userRepository) {
this.reservationRepository = reservationRepository;
this.houseRepository = houseRepository;
this.userRepository = userRepository;
}

@Transactional
public void create(Map<String, String> paymentIntentObject) {
Reservation reservation = new Reservation();

Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
Long userId = Long.valueOf(paymentIntentObject.get("userId"));

// House エンティティ取得
House house = houseRepository.getReferenceById(houseId);
User user = userRepository.getReferenceById(userId);
LocalDate reservationDate  = LocalDate.parse(paymentIntentObject.get("reservationDate"));
LocalTime reservationTime = LocalTime.parse(paymentIntentObject.get("reservationTime"));
Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));        
BigDecimal amount = new BigDecimal(paymentIntentObject.get("amount"));

// フォームからエンティティにデータを設定
reservation.setHouse(house);
reservation.setUser(user);
reservation.setReservationDate(reservationDate);
reservation.setReservationTime(reservationTime);
reservation.setNumberOfPeople(numberOfPeople);
reservation.setAmount(amount);

// 保存
reservationRepository.save(reservation);
}
    
    
}


