package com.example.kadai_002.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.House;
import com.example.kadai_002.entity.MembershipType;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.FavoriteRepository;
import com.example.kadai_002.repository.HouseRepository;
import com.example.kadai_002.security.UserDetailsImpl;
import com.example.kadai_002.service.FavoriteService;



@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final HouseRepository houseRepository;
	
	public FavoriteController (FavoriteRepository favoriteRepository, FavoriteService favoriteService, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.houseRepository = houseRepository;
	}
	
	//お気に入り一覧表示
	@GetMapping("/favorites")
	public String index (@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user,  pageable);
		     model.addAttribute("favoritePage", favoritePage);
		     return "favorites/index";
	}
	
	//お気に入り登録
		@PostMapping("/houses/{houseId}/favorites/register")
		public String register (@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes, Model model){
			House house = houseRepository.getReferenceById(houseId);
			User user = userDetailsImpl.getUser();
			
			// 有料会員以外は拒否
	        if (user.getMembershipType() != MembershipType.PAID) {
	            throw new AccessDeniedException("お気に入り登録は有料会員限定です。");
	        }
			
			favoriteService.register(house, user);
			return "redirect:/houses/{houseId}";

		}
		
		//お気に入り解除
		@PostMapping("/houses/{houseId}/favorites/{favoriteId}/delete")
		public String delete (@PathVariable(name = "favoriteId") Integer favoriteId, RedirectAttributes redirectAttributes) {
			favoriteRepository.deleteById(favoriteId);
			
			return "redirect:/houses/{houseId}";
		}

}
