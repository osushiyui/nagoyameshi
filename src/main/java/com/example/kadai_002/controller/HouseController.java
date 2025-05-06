package com.example.kadai_002.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.form.ReservationInputForm;
import com.example.kadai_002.repository.FavoriteRepository;
import com.example.kadai_002.repository.HouseRepository;
import com.example.kadai_002.repository.ReviewRepository;
import com.example.kadai_002.security.UserDetailsImpl;
import com.example.kadai_002.service.FavoriteService;
import com.example.kadai_002.service.ReviewService;

@Controller
@RequestMapping("/houses")
    public class HouseController {
	private HouseRepository houseRepository;	
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	 
	

	public HouseController(HouseRepository houseRepository,
	                       ReviewRepository reviewRepository,
	                       ReviewService reviewService, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
	    this.houseRepository = houseRepository;
	    this.reviewRepository = reviewRepository;
	    this.reviewService = reviewService;
	    this.favoriteRepository = favoriteRepository;
	    this.favoriteService = favoriteService;
	}
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
    		            @RequestParam(name = "category", required = false) String category,
                        @RequestParam(name = "priceMax", required = false) Integer priceMax,  
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
    	Page<House> housePage;
        
        if (keyword != null && !keyword.isEmpty()) {
            if ("priceMaxAsc".equals(order)) {
                housePage = houseRepository.findByNameLikeOrCategoryLikeOrderByPriceMaxAsc(keyword, keyword, pageable);
            } else {
                housePage = houseRepository.findByNameLikeOrCategoryLikeOrderByCreatedAtDesc(keyword, keyword, pageable);
            }   
        } else if (category != null && !category.isEmpty()) {  
            if ("priceMaxAsc".equals(order)) {
                housePage = houseRepository.findByCategoryLikeOrderByPriceMaxAsc(category, pageable);
            } else {
                housePage = houseRepository.findByCategoryLikeOrderByCreatedAtDesc(category, pageable);
            }
        } else if (priceMax != null) {
            if ("priceMaxAsc".equals(order)) {
                housePage = houseRepository.findByPriceMaxLessThanEqualOrderByPriceMaxAsc(priceMax, pageable);
            } else {
                housePage = houseRepository.findByPriceMaxLessThanEqualOrderByCreatedAtDesc(priceMax, pageable);
            }
        } else {
            housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
        }                
       
        model.addAttribute("housePage", housePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("priceMax", priceMax);                              
        
        return "houses/index";
    }
    
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, 
                       Model model,
                       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        House house = houseRepository.getReferenceById(id);

        boolean hasUserAlreadyReviewed = false;
        boolean hasUserAlreadyLiked = false;
        Favorite favorite = null;
        User user = null;
        
        if(userDetailsImpl != null) {
            user = userDetailsImpl.getUser();
        	hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(house,user);
        	hasUserAlreadyLiked = favoriteService.hasUserAlreadyLiked(house,user);
        	if (hasUserAlreadyLiked) {
        		favorite = favoriteRepository.findByHouseAndUser(house, user);
        	}
        	
        }
        	
        model.addAttribute("loginUser", user);
        model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
        model.addAttribute("hasUserAlreadyLiked", hasUserAlreadyLiked); 
        model.addAttribute("favorite", favorite);
        	

        List<Review> newReviews = reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
        long totalReviewCount = reviewRepository.countByHouse(house);

        model.addAttribute("house", house);
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("newReviews", newReviews);
        model.addAttribute("totalReviewCount", totalReviewCount);        
        
        return "houses/show";
    }

}


