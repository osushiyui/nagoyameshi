package com.example.kadai_002.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.repository.HouseRepository;

@Controller
@RequestMapping("/houses")
public class HouseController {
    private final HouseRepository houseRepository;        
    
    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;            
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
            if ("priceAsc".equals(order)) {
                housePage = houseRepository.findByNameLikeOrCategoryLikeOrderByPriceMaxAsc(keyword, keyword, pageable);
            } else {
                housePage = houseRepository.findByNameLikeOrCategoryLikeOrderByCreatedAtDesc(keyword, keyword, pageable);
            }         
        } else if (priceMax != null) {
            if ("priceAsc".equals(order)) {
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
}


