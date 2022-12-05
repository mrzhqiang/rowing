package com.github.mrzhqiang.rowing.module;

/**
 * 排序步骤。
 *
 * @see org.springframework.security.config.annotation.web.builders.FilterOrderRegistration.Step
 */
@SuppressWarnings("JavadocReference")
public final class OrderStep {

    private int value;

    private final int stepSize;

    public OrderStep(int initialValue, int stepSize) {
        this.value = initialValue;
        this.stepSize = stepSize;
    }

    public int next() {
        int value = this.value;
        this.value += this.stepSize;
        return value;
    }
}
