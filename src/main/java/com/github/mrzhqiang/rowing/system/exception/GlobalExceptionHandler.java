package com.github.mrzhqiang.rowing.system.exception;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.rowing.util.Views;
import com.google.common.base.VerifyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * 全局异常处理器。
 *
 * <p>
 * 捕捉 {@link org.springframework.stereotype.Controller} 注解标记的相关类所抛出的异常，进行转换处理。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEF_TRACE_ON_PRODUCTION = "请联系您的系统管理员";

    private final ExceptionLogMapper logMapper;
    private final ExceptionLogRepository logRepository;

    public GlobalExceptionHandler(ExceptionLogMapper logMapper,
                                  ExceptionLogRepository logRepository) {
        this.logMapper = logMapper;
        this.logRepository = logRepository;
    }

    /**
     * 处理客户端错误请求的异常。
     * <p>
     * 通常是客户端请求参数不对，抛出来的相关异常。
     *
     * @param ex      具体异常。
     * @param request 当前请求。
     * @return 根据请求客户端的类型返回的不同对象，浏览器一般是视图对象，而 API 则是 JSON 对象。
     */
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            VerifyException.class})
    public Object handleBadRequest(Exception ex, HttpServletRequest request) {
        log.error("Handle BAD_REQUEST by request: " + request, ex);
        return resolveException(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理请求资源未找到的异常。
     * <p>
     * 通常是资源不存在，或已被删除，或故意访问一个不存在资源导致的问题。
     *
     * @param ex      具体异常。
     * @param request 当前请求。
     * @return 根据请求客户端的类型返回的不同对象，浏览器一般是视图对象，而 API 则是 JSON 对象。
     */
    @ExceptionHandler({
            ResourceNotFoundException.class})
    public Object handleNotFound(Exception ex, HttpServletRequest request) {
        log.error("Handle NOT_FOUND by request: " + request, ex);
        return resolveException(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理内部服务错误的异常。
     * <p>
     * 一般是代码编写有误，未进行 NPE 及 index 边界检查，导致内部抛出来相关异常。
     *
     * @param ex      具体异常。
     * @param request 当前请求。
     * @return 根据请求客户端的类型返回的不同对象，浏览器一般是视图对象，而 API 则是 JSON 对象。
     */
    @ExceptionHandler({
            NullPointerException.class,
            IndexOutOfBoundsException.class,
            IllegalAccessException.class})
    public Object handleInternalServerError(Exception ex, HttpServletRequest request) {
        log.error("Handle INTERNAL_SERVER_ERROR by request: " + request, ex);
        return resolveException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理服务不可用的异常。
     * <p>
     * 一般是 IO 资源不可用，比如数据库、网络请求等出现故障，导致系统出现问题。
     *
     * @param ex      具体异常。
     * @param request 当前请求。
     * @return 根据请求客户端的类型返回的不同对象，浏览器一般是视图对象，而 API 则是 JSON 对象。
     */
    @ExceptionHandler({
            IOException.class})
    public Object handleServiceUnavailable(Exception ex, HttpServletRequest request) {
        log.error("Handle SERVICE_UNAVAILABLE by request: " + request, ex);
        return resolveException(ex, request, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * 处理其他内部服务的异常。
     * <p>
     * 注意：{@link ExceptionHandler} 注解在处理异常时，会按照异常类的 class 类型进行深度匹配，如果当前类型不满足，则匹配其父类，直到满足为止。
     * <p>
     * 最深的一层是 {@link Throwable} 类，它将被排序到 {@link Integer#MAX_VALUE} 级别，也就是最末尾的位置。
     * <p>
     * 即使你声明一个不属于 {@link Throwable} 子类的类型，也会匹配 {@link ExceptionHandlerMethodResolver#noMatchingExceptionHandler} 方法。
     * <p>
     * 当回调此方法时，它什么也不做。
     * <p>
     * 所以本方法将成为上面所有异常处理方法的后备选择，如果上面的异常类型都不匹配，则本方法将进行兜底。
     *
     * @param ex      具体异常。
     * @param request 当请求。
     * @return 根据请求客户端的类型返回的不同对象，浏览器一般是视图对象，而 API 则是 JSON 对象。
     */
    @SuppressWarnings("JavadocReference")
    @ExceptionHandler({Exception.class})
    public Object handleOther(Exception ex, HttpServletRequest request) {
        log.error("Handle other Exception by request: " + request, ex);
        return resolveException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Object resolveException(Exception exception,
                                    HttpServletRequest request,
                                    HttpStatus httpStatus) {
        ExceptionLog log = logMapper.toEntity(httpStatus, request, exception);
        logRepository.save(log);
        ExceptionLogData data = logMapper.toData(log);

        // 非调试模式下，展示异常代码会比较友好，暴露异常消息会显得很低级，且容易被发现漏洞
        if (!Environments.debug()) {
            data.setMessage(log.getCode());
            data.setTrace(DEF_TRACE_ON_PRODUCTION);
        }

        if (isHtmlRequest(request)) {
            ModelAndView view = new ModelAndView(Views.EXCEPTION_VIEW_NAME_PREFIX);
            view.setStatus(HttpStatus.resolve(data.getStatus()));
            view.addObject("data", data);
            return view;
        }

        return ResponseEntity.status(httpStatus).body(data);
    }

    private boolean isHtmlRequest(HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(it -> it.getHeader(HttpHeaders.ACCEPT))
                .filter(it -> it.contains(MediaType.TEXT_HTML_VALUE))
                .isPresent();
    }
}
