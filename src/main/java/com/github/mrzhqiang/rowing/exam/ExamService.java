package com.github.mrzhqiang.rowing.exam;

import java.util.List;

/**
 * 考试服务。
 * <p>
 */
public interface ExamService {

    void updateTakers(Long examId, List<Long> takers);

    void updateMarkers(Long examId, List<Long> markers);

}
