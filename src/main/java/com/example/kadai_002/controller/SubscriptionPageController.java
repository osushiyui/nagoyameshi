package com.example.kadai_002.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubscriptionPageController {

    @GetMapping("/subscribe")
    public String showSubscriptionPage() {
        return "subscribe/index"; 	
    }

    @GetMapping("/subscribe/success")
    public String showSuccessPage() {
        return "subscribe/success"; 
    }

    @GetMapping("/subscribe/cancel")
    public String showCancelPage() {
        return "subscribe/cancel"; 
    }
}
