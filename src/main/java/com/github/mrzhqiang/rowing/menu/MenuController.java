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
 * <p>
 * 正常情况下，应优先使用 REST 的实体返回数据，其次是投影和摘要，最后才通过控制器和接口返回自定义数据。
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping("/routes")
    public ResponseEntity<List<MenuRoute>> routes() {
        return ResponseEntity.ok(service.findRoutes());
    }

}
