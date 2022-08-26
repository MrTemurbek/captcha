package com.example.captcha.controller;

import com.example.captcha.entity.Users;
import com.example.captcha.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;




    public AuthController(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {

        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public String add(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }

    @GetMapping({"/success"})
    public String info(Authentication auth){
        return "success";
    }

    @PostMapping("/check")
    public String save(@ModelAttribute("user") Users user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(user.getCaptcha().equals(request.getSession().getAttribute("captcha"))) {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(),
                            user.getPassword());
            try {
                authenticationManager.authenticate(authInputToken);
            }catch (BadCredentialsException e){
                redirectAttributes.addFlashAttribute("errorMessage", "WRONG CREDENTIALS");
                return "redirect:/login-error";
            }
            System.out.println(jwtUtil.generateToken(user.getUsername()));
                return "/success";

        }
        redirectAttributes.addFlashAttribute("errorMessage", "WRONG CAPTCHA");
        return "redirect:/login-error";
    }


}
