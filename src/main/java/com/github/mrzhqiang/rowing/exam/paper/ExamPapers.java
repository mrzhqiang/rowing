package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 考试试卷工具。
 * <p>
 */
public final class ExamPapers {
    private ExamPapers() {
        // no instances.
    }

    /**
     * 检测是否可以考试。
     *
     * @param paper 试卷实体。
     * @return 返回 true 表示可以考试；返回 false 表示不能考试。
     */
    public static boolean checkTake(ExamPaper paper) {
        return !Boolean.TRUE.equals(paper.getFinished()) && !Boolean.TRUE.equals(paper.getSubmitted());
    }

    /**
     * 检测是否可以考试。
     *
     * @param paperInfo 试卷信息。
     * @return 返回 true 表示可以考试；返回 false 表示不能考试。
     */
    public static boolean checkTake(ExamPaperInfo paperInfo) {
        return !Boolean.TRUE.equals(paperInfo.getFinished()) && !Boolean.TRUE.equals(paperInfo.getSubmitted());
    }

    /**
     * 检测是否可以批阅。
     *
     * @param paper 试卷实体。
     * @return 返回 true 表示可以考试；返回 false 表示不能考试。
     */
    public static boolean checkMark(ExamPaper paper) {
        return !Boolean.TRUE.equals(paper.getFinished()) && Boolean.TRUE.equals(paper.getSubmitted());
    }

    /**
     * 检测是否可以批阅。
     *
     * @param paperInfo 试卷信息。
     * @return 返回 true 表示可以考试；返回 false 表示不能考试。
     */
    public static boolean checkMark(ExamPaperInfo paperInfo) {
        return !Boolean.TRUE.equals(paperInfo.getFinished()) && Boolean.TRUE.equals(paperInfo.getSubmitted());
    }

    /**
     * 根据试卷生成结果。
     *
     * @param paper 试卷实体。
     * @return 结果。
     */
    public static String generateResult(ExamPaper paper) {
        ExamRule rule = paper.getExam().getRule();
        List<ExamPaperAnswerCard> cards = paper.getCards();
        Integer questionSize = cards.stream()
                .map(ExamPaperAnswerCard::getAnswers)
                .map(List::size)
                .reduce(0, Integer::sum);
        long minutes = 0;
        if (paper.getAnswerStart() != null && paper.getSubmitTime() != null) {
            minutes = ChronoUnit.MINUTES.between(paper.getAnswerStart(), paper.getSubmitTime());
        }
        BigDecimal totalScore = paper.getTotalScore();
        boolean pass = totalScore.subtract(BigDecimal.valueOf(rule.getPassScore())).signum() >= 0;
        return Strings.lenientFormat(
                "本次考试总分 %s 分，及格分 %s 分，大题 %s 道（共计 %s 道小题）。" + System.lineSeparator() +
                        "考生答题时长 %s 分钟，总得分 %s 分，成绩：%s",
                rule.getTotalScore(), rule.getPassScore(), cards.size(), questionSize, minutes, totalScore,
                pass ? "合格" : "不合格");
    }

}
