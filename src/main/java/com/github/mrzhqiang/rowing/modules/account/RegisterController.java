package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.rowing.config.RowingSecurityProperties;
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
    private final RowingSecurityProperties rowingSecurityProperties;

    public RegisterController(AccountService accountService, RowingSecurityProperties rowingSecurityProperties) {
        this.accountService = accountService;
        this.rowingSecurityProperties = rowingSecurityProperties;
    }

    @GetMapping
    public String index(@ModelAttribute LoginForm form) {
        return "register";
    }

    @PostMapping
    public String register(@Validated @ModelAttribute RegisterForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        boolean registerSuccessful = accountService.register(form).isPresent();
        if (!registerSuccessful) {
            result.reject("RegisterController.failed");
            return "register";
        }

        return "redirect:" + rowingSecurityProperties.getLoginPath();
    }
}
