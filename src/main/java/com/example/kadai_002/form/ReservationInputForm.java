package com.example.kadai_002.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	
	private Integer houseId;
    
    private Integer userId;    
        
    @NotBlank(message = "予約日を選択してください。")
    private String reservationDate;    
    
    @NotBlank(message = "予約時間を選択してください。")
    private String reservationTime; 
    
    @NotNull(message = "予約人数を入力してください。")
    @Min(value = 1, message = "人数は1人以上に設定してください。")
    private Integer numberOfPeople; 
    
    private BigDecimal amount;
    
    // LocalDate に変換して返す
    public LocalDate getReservationDateAsDate() {
        return LocalDate.parse(reservationDate); 
    }

    // LocalTime に変換して返す
    public LocalTime getReservationTimeAsTime() {
        return LocalTime.parse(reservationTime); 
    }

}
