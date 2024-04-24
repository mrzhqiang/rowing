package com.github.mrzhqiang.rowing.setting;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setting")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService service;

    @GetMapping("/new-rsa-key")
    public RSAKeyData newRsaKey() {
        return service.createRsaKey();
    }

}
