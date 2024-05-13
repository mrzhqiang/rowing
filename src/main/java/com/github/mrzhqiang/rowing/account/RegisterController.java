package com.github.mrzhqiang.rowing.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final AccountService service;

    @PostMapping()
    public ResponseEntity<?> register(@Valid @RequestBody PasswordConfirmForm form) {
        service.register(form);
        return ResponseEntity.ok().build();
    }

}
