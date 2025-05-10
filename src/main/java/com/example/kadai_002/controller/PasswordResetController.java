package com.example.kadai_002.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.PasswordResetToken;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.PasswordResetTokenRepository;
import com.example.kadai_002.repository.UserRepository;
import com.example.kadai_002.service.EmailService;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // パスワードを忘れたフォームの表示
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
    	 return "password/forgot-password";
    }

    // メール送信処理
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = java.util.UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user);
            tokenRepository.save(resetToken);

            String resetUrl = "http://localhost:8080/reset-password?token=" + token;
            emailService.send(email, "パスワードリセット", 
                "以下のリンクからパスワードを再設定してください：\n" + resetUrl);
        }
        return "redirect:/forgot-password?sent";
    }

    // リセットリンクをクリックされたときの画面表示
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            return "reset-password-invalid";
        }
        model.addAttribute("token", token);
        return "password/reset-password";
    }

    // 新しいパスワードの受け取りと更新
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            return "reset-password-invalid";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); 
        return "redirect:/login?resetSuccess";
    }
}
