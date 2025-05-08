package com.example.kadai_002.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.House;
import com.example.kadai_002.form.HouseEditForm;
import com.example.kadai_002.form.HouseRegisterForm;
import com.example.kadai_002.repository.HouseRepository;
import com.example.kadai_002.service.HouseService;

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
private final HouseRepository houseRepository;  
private final HouseService houseService; 
    
public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
    this.houseRepository = houseRepository; 
    this.houseService = houseService;  
}		
    
    @GetMapping
    public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
    	Page<House> housePage;
    	
    	if (keyword != null && !keyword.isEmpty()) {
            housePage = houseRepository.findByCategoryLike("%" + keyword + "%", pageable);                
        } else {
            housePage = houseRepository.findAll(pageable);
        }  
    	
    	model.addAttribute("housePage", housePage);
    	model.addAttribute("keyword", keyword);
    	
    	
        return "admin/houses/index";
    }  

    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, Model model) {
        House house = houseRepository.getReferenceById(id);
        
        model.addAttribute("house", house);
        
        return "admin/houses/show";
    }    
    
    @GetMapping("/showPrice")
    public String showPrice(Model model) {
        // 例としてIDが1のHouseデータを取得
        House house = houseRepository.findById(1L).orElse(null);

        if (house != null) {
            model.addAttribute("house", house);
        } else {
            model.addAttribute("message", "データが見つかりません");
        }

        return "priceView"; // priceView.htmlに遷移
    }
    
    
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("houseRegisterForm", new HouseRegisterForm());
        return "admin/houses/register";
    } 
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/houses/register";
        }
        
        houseService.create(houseRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
        
        return "redirect:/admin/houses";
    }  
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Long id, Model model) {
        House house = houseRepository.getReferenceById(id);
        String imageName = house.getImageName();

        HouseEditForm houseEditForm = new HouseEditForm(
            house.getId(),
            house.getName(),
            null, 
            house.getDescription(),
            house.getPriceMin(),
            house.getPriceMax(),
            house.getStartTime(),
            house.getEndTime(),
            house.getHoliday(),
            house.getPostalCode(),
            house.getAddress(),
            house.getPhoneNumber(),
            house.getCategory()
        );

        model.addAttribute("imageName", imageName);
        model.addAttribute("houseEditForm", houseEditForm);

        return "admin/houses/edit";
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/houses/edit";
        }
        
        houseService.update(houseEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/admin/houses";
    }    

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {        
        houseRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/admin/houses";
    }    
	
    
  
}
