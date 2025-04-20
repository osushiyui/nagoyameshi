package com.example.kadai_002.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {    
    private Long houseId;
        
    private Long userId;    
        
    private String reservationDate;      
    
    private Integer numberOfPeople;
    
    private Integer amount;

}