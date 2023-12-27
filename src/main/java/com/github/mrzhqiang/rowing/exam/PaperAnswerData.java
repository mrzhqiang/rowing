package com.github.mrzhqiang.rowing.exam;

import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷答题数据。
 * <p>
 */
@Data
public class PaperAnswerData {

    private Long id;
    private Long selectOptionId;
    private List<Long> chooseOptionIds = Lists.newArrayList();
    private String answer;
    private String answerUrl;
    private BigDecimal score;
    private String comment;

}
