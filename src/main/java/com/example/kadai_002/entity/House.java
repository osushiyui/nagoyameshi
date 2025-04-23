package com.example.kadai_002.entity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "houses")
@Data
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "description")
    private String description;

    @Column(name = "price_max")
    private Integer priceMax;
    
    @Column(name = "price_min")
    private Integer priceMin;

    @Column(name = "start_time")
    private Integer startTime;
    
    @Column(name = "end_time")
    private Integer endTime;

    @Column(name = "holiday")
    private String holiday;


    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "category")
    private String category;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
    
    public String getHolidayDisplay() {
        // 曜日のリスト
        List<String> list = Arrays.asList("日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日");

        // holiday が "1" ～ "7" の場合、それに対応する曜日を返す
        if (this.holiday != null && this.holiday.matches("[1-7]")) {
            int index = Integer.parseInt(this.holiday) - 1; // 文字列から整数に変換してインデックスを計算
            return list.get(index); // 該当する曜日を返す
        } else {
            return "不明"; // 範囲外や不正な値の場合は "不明" を返す
        }
	}
    
}
