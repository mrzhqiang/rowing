package com.github.mrzhqiang.rowing.exam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考试控制器。
 * <p>
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    @PutMapping("/{id}/takers")
    public ResponseEntity<?> takers(@PathVariable Long id,
                                    @RequestBody List<Long> takers) {
        service.updateTakers(id, takers);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/markers")
    public ResponseEntity<?> markers(@PathVariable Long id,
                                     @RequestBody List<Long> markers) {
        service.updateMarkers(id, markers);
        return ResponseEntity.ok().build();
    }

}
