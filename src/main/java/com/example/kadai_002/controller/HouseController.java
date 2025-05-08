package com.example.kadai_002.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	                    @PageableDefault(page = 0, size = 10) Pageable pageable,
	                    Model model) {

	    Page<House> housePage;
	    
	    // ソート条件を明示的に定義
	    Pageable sortedPageable;
	    if ("priceMaxAsc".equals(order)) {
	        sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "priceMax"));
	    } else {
	        sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
	    }

	    // 検索条件に応じて処理
	    if (keyword != null && !keyword.isEmpty()) {
	        housePage = houseRepository.findByNameLikeOrCategoryLike("%" + keyword + "%", "%" + keyword + "%", sortedPageable);
	    } else if (category != null && !category.isEmpty()) {
	        housePage = houseRepository.findByCategoryLike("%" + category + "%", sortedPageable);
	    } else if (priceMax != null) {
	        housePage = houseRepository.findByPriceMaxLessThanEqual(priceMax, sortedPageable);
	    } else {
	        housePage = houseRepository.findAll(sortedPageable);
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
        	model.addAttribute("isPaidUser", user.isUserPaid());
        } else {
            model.addAttribute("isPaidUser", false);
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


