package com.github.mrzhqiang.rowing.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/userInfo")
    public HttpEntity<UserInfoData> userInfo(@AuthenticationPrincipal UserDetails details) {
        UserInfoData data = service.findByUsername(details.getUsername());
        return ResponseEntity.ok(data);
    }

}
