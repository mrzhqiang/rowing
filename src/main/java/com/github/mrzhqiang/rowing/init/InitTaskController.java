package com.github.mrzhqiang.rowing.init;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 初始化任务控制器。
 * <p>
 */
@RestController
@RequestMapping("/init-task")
public class InitTaskController {

    private final InitTaskService service;

    public InitTaskController(InitTaskService service) {
        this.service = service;
    }

    @GetMapping("/execute")
    public ResponseEntity<?> execute(@RequestParam String path) {
        service.executeByPath(path);
        return ResponseEntity.ok().build();
    }

}
