package com.github.mrzhqiang.rowing.modules.init;

/**
 * 初始化异常。
 * <p>
 * 通常用来回滚初始化操作中的事务，同时避免丢失初始化日志，所以需要这一类的异常。
 */
public final class InitializationException extends RuntimeException {

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
