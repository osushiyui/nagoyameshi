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
import org.springframework.web.multipart.MultipartFile;
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
            housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);                
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
   
    public void updateHouseFromForm(HouseEditForm form, House house) {
        house.setName(form.getName());
        
        // 画像ファイルが存在する場合は、その処理を行う（例: ファイルの保存とファイル名の設定）
        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {
            String imageName = saveImageFile(form.getImageFile());
            house.setImageName(imageName);
        }

        house.setDescription(form.getDescription());
        
        // フォームから受け取った `price` を `price_min` および `price_max` 両方に設定する例（必要に応じて調整）
        house.setPriceMin(form.getPrice());
        house.setPriceMax(form.getPrice());

        // `time` フィールドは一日を通しての時間範囲を示します。具体的には開始時間と終了時間に適用する
        house.setStartTime(form.getTime()); // 例: `time` は `start_time` を意味すると仮定している
        house.setEndTime(form.getTime() + 8); // `end_time` はビジネスロジックに基づいて計算するべき

        house.setHoliday(form.getHoliday());
        house.setPostalCode(form.getPostalCode());
        house.setAddress(form.getAddress());
        house.setPhoneNumber(form.getPhoneNumber());
        house.setCategory(form.getCategory());
    }

	private String saveImageFile(MultipartFile imageFile) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
  
}
