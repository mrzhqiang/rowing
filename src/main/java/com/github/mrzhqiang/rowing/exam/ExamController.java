package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.exam.paper.ExamPaperInfo;
import com.google.common.base.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/{id}/prepare")
    public ResponseEntity<?> prepare(@PathVariable Long id) {
        service.prepare(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/for-taker")
    public ResponseEntity<Page<ExamInfo>> takerExam(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam(name = "title") String title,
                                                    @RequestParam(name = "code") String code,
                                                    @RequestParam(name = "firstStart") LocalDateTime firstStart,
                                                    @RequestParam(name = "secondStart") LocalDateTime secondStart,
                                                    @PageableDefault(20) Pageable pageable) {
        return ResponseEntity.ok(service.takerExamInfo(userDetails, title, code, firstStart, secondStart, pageable));
    }

    @GetMapping("/for-marker")
    public ResponseEntity<Page<ExamInfo>> markerExam(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(name = "title") String title,
                                                     @RequestParam(name = "code") String code,
                                                     @RequestParam(name = "firstStart") LocalDateTime firstStart,
                                                     @RequestParam(name = "secondStart") LocalDateTime secondStart,
                                                     @PageableDefault(20) Pageable pageable) {
        return ResponseEntity.ok(service.markerExamInfo(userDetails, title, code, firstStart, secondStart, pageable));
    }

    @GetMapping("/{id}/taker-paper-status")
    public ResponseEntity<Boolean> takerPaperStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable Long id) {
        ExamPaperInfo takerPaper = service.findTakerPaper(userDetails.getUsername(), id);
        return ResponseEntity.ok(!takerPaper.getFinished() && takerPaper.getSubmitted());
    }

    @GetMapping("/{id}/taker-paper")
    public ResponseEntity<ExamPaperInfo> takerPaper(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable Long id) {
        ExamPaperInfo takerPaper = service.findTakerPaper(userDetails.getUsername(), id);
        service.takerPaper(Long.parseLong(takerPaper.getId()));
        return ResponseEntity.ok(takerPaper);
    }

    @GetMapping("/{id}/marker-paper")
    public ResponseEntity<ExamPaperInfo> markerPaper(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable Long id) {
        ExamPaperInfo markerPaper = service.findMarkerPaper(userDetails.getUsername(), id);
        service.markerPaper(Long.parseLong(markerPaper.getId()));
        return ResponseEntity.ok(markerPaper);
    }

    @PatchMapping("/{id}/paper-answer")
    public ResponseEntity<?> savePaperAnswer(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long id,
                                             @RequestBody PaperAnswerData data) {
        ExamPaperInfo takerPaper = service.findTakerPaper(userDetails.getUsername(), id);
        Preconditions.checkState(Boolean.FALSE.equals(takerPaper.getSubmitted()), "您已交卷，请不要重复操作");
        service.savePaperAnswer(data);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/paper-marker")
    public ResponseEntity<?> savePaperMarker(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long id,
                                             @RequestBody PaperAnswerData data) {
        ExamPaperInfo markerPaper = service.findMarkerPaper(userDetails.getUsername(), id);
        Preconditions.checkState(Boolean.FALSE.equals(markerPaper.getFinished()), "您已完成批阅，请不要重复操作");
        service.savePaperMarker(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/commit-paper")
    public ResponseEntity<?> commitPaper(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long id,
                                         @RequestBody PaperAnswerData data) {
        ExamPaperInfo takerPaper = service.findTakerPaper(userDetails.getUsername(), id);
        Preconditions.checkState(Boolean.FALSE.equals(takerPaper.getSubmitted()), "请不要重复交卷");
        service.commitPaper(Long.parseLong(takerPaper.getId()), data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finish-paper")
    public ResponseEntity<?> finishPaper(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long id,
                                         @RequestBody PaperAnswerData data) {
        ExamPaperInfo markerPaper = service.findMarkerPaper(userDetails.getUsername(), id);
        Preconditions.checkState(Boolean.FALSE.equals(markerPaper.getFinished()), "请不要重复完成批阅");
        service.finishPaper(Long.parseLong(markerPaper.getId()), data);
        return ResponseEntity.ok().build();
    }

}
