package com.github.mrzhqiang.rowing.modules.init;

/**
 * 排序步骤。
 *
 * @see org.springframework.security.config.annotation.web.builders.FilterOrderRegistration.Step
 */
@SuppressWarnings("JavadocReference")
final class OrderStep {

    private int value;

    private final int stepSize;

    OrderStep(int initialValue, int stepSize) {
        this.value = initialValue;
        this.stepSize = stepSize;
    }

    int next() {
        int value = this.value;
        this.value += this.stepSize;
        return value;
    }
}
