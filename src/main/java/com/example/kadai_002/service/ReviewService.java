package com.example.kadai_002.service;

import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.form.ReviewEditForm;
import com.example.kadai_002.form.ReviewRegisterForm;
import com.example.kadai_002.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository  reviewRepository) {
        this. reviewRepository = reviewRepository;
    
}
    
    //作成
    @Transactional
	public void create(House house, User user, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();

		review.setHouse(house);                
		review.setUser(user);
		review.setScore(reviewRegisterForm.getScore());
		review.setComment(reviewRegisterForm.getComment());
               
		reviewRepository.save(review);
    }
    


    //編集
    @Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

		review.setScore(reviewEditForm.getScore());                
		review.setComment(reviewEditForm.getComment());
               
		reviewRepository.save(review);

	}
    
    //レビュー済みかどうか
    public boolean hasUserAlreadyReviewed(House house, User user) {
    	return reviewRepository.findByHouseAndUser(house, user) !=null;
    }
}
