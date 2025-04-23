package com.example.kadai_002.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRegisterForm {
	@NotNull(message = "評価を入力してください。")
	private Integer score;
	
	@NotBlank(message = "コメントを入力してください。")
	private String comment;

}
