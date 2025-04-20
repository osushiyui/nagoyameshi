package com.example.kadai_002.service;

import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	//お気に入り登録
	@Transactional
	public void register (House house, User user) {
		Favorite favorite = new Favorite();
		
		favorite.setHouse(house);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
		
	}
	
	//お気に入り登録済みかどうか
	public boolean hasUserAlreadyLiked(House house, User user) {
		return favoriteRepository.findByHouseAndUser(house, user) != null;
	}

}
