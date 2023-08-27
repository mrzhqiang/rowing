package com.github.mrzhqiang.rowing.init;

/**
 * 初始化异常。
 * <p>
 * 通常用来回滚初始化操作中的事务，同时避免丢失初始化日志。
 */
public final class InitializationException extends RuntimeException {

    private static final long serialVersionUID = 4837612797180607239L;

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

}
