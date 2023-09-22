package com.github.mrzhqiang.rowing.exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试控制器。
 * <p>
 * 由于不是 {@link org.springframework.data.rest.webmvc.RepositoryRestController} 注解标记的控制器，
 * REST 相关的特性可能有部分缺失，因此需要手动进行操作。
 * 比如 {@link org.springframework.data.rest.webmvc.json.EnumTranslator 枚举翻译器} 等。
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

    @GetMapping("/my-exam")
    public ResponseEntity<Page<ExamInfo>> myExam(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam(name = "title") String title,
                                                 @RequestParam(name = "code") String code,
                                                 @RequestParam(name = "firstStart") LocalDateTime firstStart,
                                                 @RequestParam(name = "secondStart") LocalDateTime secondStart,
                                                 @PageableDefault(20) Pageable pageable) {
        return ResponseEntity.ok(service.myExamInfo(userDetails, title, code, firstStart, secondStart, pageable));
    }

}
