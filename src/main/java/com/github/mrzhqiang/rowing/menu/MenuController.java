package com.github.mrzhqiang.rowing.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单控制器。
 * <p>
 * 自定义的菜单控制器，用于提供复杂接口。
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<List<MenuData>> list() {
        return ResponseEntity.ok(service.listRoot());
    }

}
