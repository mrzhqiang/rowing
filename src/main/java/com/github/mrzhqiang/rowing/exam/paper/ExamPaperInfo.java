package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.account.AccountInfo;
import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷信息。
 * <p>
 * TODO 区分考生试卷和阅卷试卷
 */
@Projection(name = "exam-paper-info", types = {ExamPaper.class})
public interface ExamPaperInfo extends BaseProjection {

    AccountInfo getTaker();

    AccountInfo getMarker();

    LocalDateTime getAnswerStart();

    Boolean getSubmitted();

    LocalDateTime getSubmitTime();

    LocalDateTime getMarkStart();

    Boolean getFinished();

    LocalDateTime getFinishTime();

    String getResult();

    BigDecimal getTotalScore();

    List<ExamPaperAnswerCardInfo> getCards();

}
