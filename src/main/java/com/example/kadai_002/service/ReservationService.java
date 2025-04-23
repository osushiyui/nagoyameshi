package com.example.kadai_002.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.form.ReservationInputForm;
import com.example.kadai_002.repository.HouseRepository;
import com.example.kadai_002.repository.ReservationRepository;

@Service
public class ReservationService {
	
    private final ReservationRepository reservationRepository;
    private final HouseRepository houseRepository;

    
    public ReservationService(ReservationRepository reservationRepository,
            HouseRepository houseRepository) {
this.reservationRepository = reservationRepository;
this.houseRepository = houseRepository;
}

@Transactional
public void create(ReservationInputForm form) { //予約の処理
Reservation reservation = new Reservation();

// House エンティティ取得
House house = houseRepository.findById(form.getHouseId().longValue())
.orElseThrow(() -> new RuntimeException("店舗が存在しません"));

// フォームからエンティティにデータを設定
reservation.setHouse(house);
reservation.setUserId(form.getUserId());
reservation.setReservationDate(form.getReservationDateAsDate());
reservation.setReservationTime(form.getReservationTimeAsTime());
reservation.setNumberOfPeople(form.getNumberOfPeople());
reservation.setAmount(form.getAmount());

// 保存
reservationRepository.save(reservation);
}
    //public Reservation findById(Long reservationId) {
       // return reservationRepository.findById(reservationId).orElse(null);
    
}


