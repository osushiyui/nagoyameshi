package com.example.kadai_002.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseEditForm {
    @NotNull
    private Long id;    

    @NotBlank(message = "店舗名を入力してください。")
    private String name;

    private MultipartFile imageFile;

    @NotBlank(message = "説明を入力してください。")
    private String description;

    @NotNull(message = "最低料金を入力してください。")
    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。")
    private Integer priceMin;

    @NotNull(message = "最高料金を入力してください。")
    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。")
    private Integer priceMax;

    @NotNull(message = "開始時間を入力してください。")
    private Integer startTime;

    @NotNull(message = "終了時間を入力してください。")
    private Integer endTime;

    @NotBlank(message = "定休日を入力してください。")
    private String holiday;

    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;

    @NotBlank(message = "住所を入力してください。")
    private String address;

    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;

    @NotBlank(message = "カテゴリを入力してください。")
    private String category;
}
