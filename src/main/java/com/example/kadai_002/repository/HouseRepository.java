package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {
	public Page<House> findByNameLike(String keyword, Pageable pageable);

	 public Page<House> findByNameLikeOrCategoryLike(String nameKeyword, String categoryKeyword, Pageable pageable);  
	    public Page<House> findByNameLikeOrCategoryLikeOrderByCreatedAtDesc(String nameKeyword, String categoryKeyword, Pageable pageable);  
	    public Page<House> findByNameLikeOrCategoryLikeOrderByPriceMaxAsc(String nameKeyword, String categoryKeyword, Pageable pageable);  
	    public Page<House> findByCategoryLikeOrderByCreatedAtDesc(String category, Pageable pageable);
	    public Page<House> findByCategoryLikeOrderByPriceMaxAsc(String category, Pageable pageable);
	    public Page<House> findByPriceMaxLessThanEqualOrderByCreatedAtDesc(Integer priceMax, Pageable pageable);
	    public Page<House> findByPriceMaxLessThanEqualOrderByPriceMaxAsc(Integer priceMax, Pageable pageable); 
	    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);
	    public Page<House> findAllByOrderByPriceMaxAsc(Pageable pageable);
	}

