package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionState;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.session.SessionDetails;
import com.github.mrzhqiang.rowing.session.SessionDetailsService;
import com.github.mrzhqiang.rowing.session.Sessions;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.github.mrzhqiang.rowing.util.Jsons;
import com.github.mrzhqiang.rowing.util.Validations;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.reactivex.observers.DefaultObserver;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

/**
 * 操作处理器。
 * <p>
 * 利用 AOP 机制，自动记录标记为 {@link Action 操作} 方法的执行情况。
 */
@Slf4j
@Aspect
@Component
public class ActionAspect {

    private static final char DOT_CHAR = ',';
    private static final String PARAMETERS_TEMPLATE = "%s=%s";
    private static final String NO_CONTENT = "(void)";

    private final ActionLogRepository repository;
    private final SessionDetailsService sessionDetailsService;
    private final EnumTranslator translator;

    public ActionAspect(ActionLogRepository repository,
                        SessionDetailsService sessionDetailsService,
                        EnumTranslator translator) {
        this.repository = repository;
        this.sessionDetailsService = sessionDetailsService;
        this.translator = translator;
    }

    @Pointcut("@annotation(com.github.mrzhqiang.rowing.action.Action)")
    public void actionPoint() {
    }

    @AfterReturning(pointcut = "actionPoint()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        handleAction(point, result, null);
    }

    @AfterThrowing(pointcut = "actionPoint()", throwing = "exception")
    public void afterThrowing(JoinPoint point, Exception exception) {
        handleAction(point, null, exception);
    }

    public void handleAction(JoinPoint point, Object result, Exception exception) {
        Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
        Action methodAction = AnnotationUtils.findAnnotation(targetMethod, Action.class);
        ActionType type = Optional.ofNullable(methodAction).map(Action::type).orElse(ActionType.NONE);
        String action = Optional.ofNullable(methodAction).map(Action::value).orElseGet(() -> translator.asText(type));

        Class<?> targetClass = point.getTarget().getClass();
        String target = Optional.ofNullable(targetClass.getCanonicalName()).orElseGet(targetClass::getName);
        target = target.substring(0, Math.min(Domains.CLASS_NAME_LENGTH, target.length()));

        String method = targetMethod.getName();
        method = method.substring(0, Math.min(Domains.METHOD_NAME_LENGTH, method.length()));

        Parameter[] parameters = targetMethod.getParameters();
        Object[] args = point.getArgs();
        List<String> paramList = Lists.newArrayListWithCapacity(parameters.length);
        for (int i = 0; i < parameters.length && i < args.length; i++) {
            String name = parameters[i].getName();
            Object arg = args[i];
            paramList.add(Strings.lenientFormat(PARAMETERS_TEMPLATE, name, arg));
        }
        String params = Joiner.on(DOT_CHAR).join(paramList);
        if (StringUtils.hasLength(params)) {
            params = params.substring(0, Math.min(Domains.METHOD_PARAMS_LENGTH, params.length()));
        }

        ActionState state = ActionState.UNKNOWN;
        String resultContent = NO_CONTENT;
        if (result != null) {
            state = ActionState.PASSING;
            resultContent = Jsons.prettyPrint(Jsons.toJson(result));
        }
        if (exception != null) {
            state = ActionState.FAILED;
            resultContent = Validations.findMessage(exception);
        }

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(action);
        actionLog.setType(type);
        actionLog.setTarget(target);
        actionLog.setMethod(method);
        actionLog.setParams(params);
        actionLog.setState(state);
        actionLog.setResult(resultContent);
        Optional<SessionDetails> sessionDetails = Sessions.ofCurrentDetails();
        if (sessionDetails.isPresent()) {
            SessionDetails details = sessionDetails.get();
            actionLog.setIp(details.getIp());
            actionLog.setLocation(details.getLocation());
            actionLog.setDevice(details.getAccessType());
            repository.save(actionLog);
            return;
        }
        // 可能还没查到 IP 地址的地理位置，此时手动发起转换请求
        String host = Authentications.currentHost();
        if ("127.0.0.1".equals(host) || "localhost".equals(host)) {
            actionLog.setIp(host);
            actionLog.setLocation("本机地址");
            actionLog.setDevice("本地机器");
            repository.save(actionLog);
            return;
        }
        if (!Authentications.UNKNOWN_HOST.equals(host)) {
            sessionDetailsService.observeApi(host)
                    .onErrorResumeNext(sessionDetailsService.observeDb(host))
                    .subscribe(new DefaultObserver<SessionDetails>() {
                        @Override
                        public void onNext(@Nonnull SessionDetails details) {
                            actionLog.setIp(details.getIp());
                            actionLog.setLocation(details.getLocation());
                            actionLog.setDevice(details.getAccessType());
                            repository.save(actionLog);
                        }

                        @Override
                        public void onError(@Nonnull Throwable e) {
                            log.error("无法为 {} 找到对应地址，可能是：{} 问题", host, e.getLocalizedMessage());
                        }

                        @Override
                        public void onComplete() {
                            // do nothing
                        }
                    });
        }
    }
}
