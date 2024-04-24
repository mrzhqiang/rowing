package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import lombok.experimental.UtilityClass;

/**
 * 初始化工具。
 */
@UtilityClass
public class InitTasks {

    /**
     * 检测任务是否可以跳过。
     *
     * @param task 初始化任务。
     * @return 返回 true 表示可以跳过；否则表示不可以跳过。
     */
    public static boolean checkSkip(InitTask task) {
        // 对于所有任务：如果它不存在，或已废弃，或已经开始执行，则可以跳过执行
        return task == null || Logic.YES.equals(task.getDiscard()) || TaskStatus.STARTED.equals(task.getStatus());
    }

    /**
     * 找到初始化器完成时的任务状态。
     *
     * @param initializer 初始化器。
     * @return 任务状态。
     */
    public static TaskStatus findCompletedStatus(Initializer initializer) {
        // 如果是每次运行模式，那么设置初始化任务为默认状态，否则设置为已完成状态（表示只执行一次）
        return TaskMode.EACH.equals(initializer.getMode()) ? TaskStatus.DEFAULT : TaskStatus.COMPLETED;
    }

    /**
     * 找到初始化器失败时的任务状态。
     *
     * @param initializer 初始化器。
     * @return 任务状态。
     */
    public static TaskStatus findFailedStatus(Initializer initializer) {
        // 如果是每次运行模式，那么设置初始化任务为默认状态，否则设置为失败状态（表示只执行一次）
        return TaskMode.EACH.equals(initializer.getMode()) ? TaskStatus.DEFAULT : TaskStatus.FAILED;
    }

}
