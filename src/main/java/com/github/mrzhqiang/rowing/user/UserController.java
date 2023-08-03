package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.CurrentAccount;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/userInfo")
    public HttpEntity<UserInfoData> userInfo(@CurrentAccount Account account) {
        UserInfoData data = service.findByOwner(account);
        return ResponseEntity.ok(data);
    }
}
