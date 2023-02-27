package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.SystemUserScope;
import com.github.mrzhqiang.rowing.security.WithSystemUser;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动初始化器。
 * <p>
 * 支持在系统启动时自动执行，区分手动执行的初始化器。
 * <p>
 * 应该在 {@link InitializationOrderRegistration} 中对实现类进行顺序注册，否则无法保证执行顺序。
 * <p>
 * 注意：实现类必须标记为 {@link Component}，否则无法被记录到数据库，并且无法自动执行。
 * <p>
 * 关于 {@link Ordered} 接口：用于对初始化顺序进行排序。
 */
public abstract class AutoInitializer implements Initializer, Ordered {

    /**
     * 自动运行。
     * <p>
     * 这里是扩展类需要实现的方法，使其专注于业务逻辑。
     * <p>
     * 支持抛出任何异常，最终会转化为初始化异常。
     */
    protected abstract void autoRun() throws Exception;

    /**
     * 执行初始化。
     * <p>
     * 关于事务：只有遇到 InitializationException 异常才会回滚，并且每次被调用时，创建新的事务。
     * <p>
     * 关于系统用户：自动初始化在系统启动时执行，可能由于未认证而无法执行，需要一个默认的认证用户。
     */
    @WithSystemUser(scope = SystemUserScope.CURRENT)
    @Transactional(rollbackFor = InitializationException.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void execute() {
        try {
            this.autoRun();
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public final boolean isAutoExecute() {
        return true;
    }

    @Override
    public int getOrder() {
        // 从注册登记中获得执行顺序，有执行要求的初始化器必须注册登记
        return InitializationOrderRegistration.find(this);
    }
}
