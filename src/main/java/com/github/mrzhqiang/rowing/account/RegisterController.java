package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.config.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AccountService accountService;
    private final SecurityProperties securityProperties;

    public RegisterController(AccountService accountService, SecurityProperties securityProperties) {
        this.accountService = accountService;
        this.securityProperties = securityProperties;
    }

    @GetMapping
    public String index(@ModelAttribute AccountForm form) {
        return "register";
    }

    @PostMapping
    public String register(@Validated @ModelAttribute AccountForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        boolean registerSuccessful = accountService.register(form);
        if (!registerSuccessful) {
            result.reject("RegisterController.failed");
            return "register";
        }

        return "redirect:" + securityProperties.getLoginPath();
    }
}