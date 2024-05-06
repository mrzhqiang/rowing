package com.github.mrzhqiang.rowing.exception;

/**
 * 资源未找到异常。
 * <p>
 * 主要用于抛出异常给 {@link GlobalExceptionHandler} 进行 404 处理。
 */
public final class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3530787478941887904L;

    private ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String message) {
        return new ResourceNotFoundException(message);
    }

}
