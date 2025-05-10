package com.example.kadai_002.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MembershipController {

    @GetMapping("/membership/upgrade")
    public String showUpgradePage(Model model) {
        return "membership/upgrade"; 
    }
}
