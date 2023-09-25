package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.exam.paper.ExamPaperInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试服务。
 * <p>
 */
public interface ExamService {

    /**
     * 更新考生名单。
     *
     * @param examId 考试 ID。
     * @param takers 最新的考生名单。
     */
    void updateTakers(Long examId, List<Long> takers);

    /**
     * 更新阅卷人名单。
     *
     * @param examId  考试 ID。
     * @param markers 最新的阅卷人名单。
     */
    void updateMarkers(Long examId, List<Long> markers);

    /**
     * 查询考生的考试信息分页数据。
     *
     * @param userDetails 当前登录用户。
     * @param title       模糊查询的标题。
     * @param code        模糊查询的编码。
     * @param firstStart  查询考试时间的起始区间。
     * @param secondStart 查询考试时间的结束区间。
     * @param pageable    分页参数。
     * @return 考试信息分页数据。
     */
    Page<ExamInfo> takerExamInfo(UserDetails userDetails,
                                 String title,
                                 String code,
                                 LocalDateTime firstStart,
                                 LocalDateTime secondStart,
                                 Pageable pageable);

    /**
     * 查询阅卷人的考试信息分页数据。
     *
     * @param userDetails 当前登录用户。
     * @param title       模糊查询的标题。
     * @param code        模糊查询的编码。
     * @param firstStart  查询考试时间的起始区间。
     * @param secondStart 查询考试时间的结束区间。
     * @param pageable    分页参数。
     * @return 考试信息分页数据。
     */
    Page<ExamInfo> markerExamInfo(UserDetails userDetails,
                                  String title,
                                  String code,
                                  LocalDateTime firstStart,
                                  LocalDateTime secondStart,
                                  Pageable pageable);

    /**
     * 准备考试。
     *
     * @param id 考试 ID。
     */
    void prepare(Long id);

    /**
     * 更新考试状态。
     * <p>
     * 1. 从准备到进行中：当前时间在考试开始时间与结束时间之间时更新。
     * <p>
     * 2. 从进行中到阅卷中：当前时间在考试结束时间之后时更新。
     * <p>
     * 3. 从阅卷中到已完成：所有试卷都已批阅完毕时更新。
     */
    void updateStatus();

    /**
     * 找到指定考生的试卷信息。
     *
     * @param username 考生用户名。
     * @param examId   考试 ID。
     * @return 考试试卷信息。
     */
    ExamPaperInfo findTakerPaper(String username, Long examId);

    /**
     * 开始考试。
     *
     * @param paperId 试卷 ID。
     */
    void takerPaper(Long paperId);

    /**
     * 找到指定阅卷人的试卷信息。
     *
     * @param username 阅卷人用户名。
     * @param examId   考试 ID。
     * @return 考试试卷信息。
     */
    ExamPaperInfo findMarkerPaper(String username, Long examId);

    /**
     * 开始批阅。
     *
     * @param paperId 试卷 ID。
     */
    void markerPaper(Long paperId);

    /**
     * 保存试卷答题数据。
     *
     * @param data 试卷答题数据。
     */
    void savePaperAnswer(PaperAnswerData data);

    /**
     * 保存试卷批阅数据。
     *
     * @param data 试卷批阅数据。
     */
    void savePaperMarker(PaperAnswerData data);

    /**
     * 提交试卷。
     *
     * @param paperId 试卷 ID。
     * @param data    最后的试卷答题数据。
     */
    void commitPaper(Long paperId, PaperAnswerData data);

    /**
     * 完成批阅。
     *
     * @param parseLong 试卷 ID。
     * @param data      最后的试题批阅数据。
     */
    void finishPaper(Long parseLong, PaperAnswerData data);

}
