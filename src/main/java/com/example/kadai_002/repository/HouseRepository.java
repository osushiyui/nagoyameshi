package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {

    
    Page<House> findByNameLikeOrCategoryLike(String nameKeyword, String categoryKeyword, Pageable pageable);
    
    Page<House> findByCategoryLike(String category, Pageable pageable);

    Page<House> findByPriceMaxLessThanEqual(Integer priceMax, Pageable pageable);

    Page<House> findAll(Pageable pageable);

    List<House> findTop10ByOrderByCreatedAtDesc();

    House getReferenceById(Integer id);
}
