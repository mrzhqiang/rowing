package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.config.SecurityProperties;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//@Controller
//@RequestMapping("/register")
public class RegisterController {

    private final AccountService accountService;
    private final SecurityProperties securityProperties;

    public RegisterController(AccountService accountService, SecurityProperties securityProperties) {
        this.accountService = accountService;
        this.securityProperties = securityProperties;
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

        return "redirect:" + securityProperties.getLoginPath();
    }
}
