package com.github.mrzhqiang.rowing.core.action;

import com.github.mrzhqiang.helper.StackTraces;
import com.github.mrzhqiang.rowing.core.session.SessionDetails;
import com.github.mrzhqiang.rowing.core.session.SessionDetailsService;
import com.github.mrzhqiang.rowing.core.session.Sessions;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.github.mrzhqiang.rowing.util.Jsons;
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
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ActionLogHandler {

    private static final String UNKNOWN_ACTION = "(unknown-action)";
    private static final String PARAMETERS_TEMPLATE = "%s=%s";
    private static final char DOT_CHAR = ',';

    private final ActionLogRepository repository;
    private final SessionDetailsService sessionDetailsService;

    public ActionLogHandler(ActionLogRepository repository,
                            SessionDetailsService sessionDetailsService) {
        this.repository = repository;
        this.sessionDetailsService = sessionDetailsService;
    }

    @Pointcut("@annotation(com.github.mrzhqiang.rowing.core.action.Action)")
    public void actionPoint() {
    }

    @AfterReturning(pointcut = "actionPoint()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        handleAction(point, result, null);
    }

    @AfterThrowing(pointcut = "actionPoint()", throwing = "throwable")
    public void afterThrowing(JoinPoint point, Throwable throwable) {
        handleAction(point, null, throwable);
    }

    public void handleAction(JoinPoint point, Object result, Throwable throwable) {
        Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
        Action methodAction = AnnotationUtils.findAnnotation(targetMethod, Action.class);
        String action = Optional.ofNullable(methodAction).map(Action::value).orElse(UNKNOWN_ACTION);
        ActionType type = Optional.ofNullable(methodAction).map(Action::type).orElse(ActionType.NONE);

        Class<?> targetClass = point.getTarget().getClass();
        String target = Optional.ofNullable(targetClass.getCanonicalName()).orElseGet(targetClass::getName);
        String method = targetMethod.getName();

        Parameter[] parameters = targetMethod.getParameters();
        Object[] args = point.getArgs();
        List<String> paramList = Lists.newArrayListWithCapacity(parameters.length);
        for (int i = 0; i < parameters.length && i < args.length; i++) {
            String name = parameters[i].getName();
            Object arg = args[i];
            paramList.add(Strings.lenientFormat(PARAMETERS_TEMPLATE, name, arg));
        }
        String params = Joiner.on(DOT_CHAR).join(paramList);

        ActionState state = ActionState.UNKNOWN;
        String resultContent = "(no-content)";
        if (result != null) {
            state = ActionState.PASSING;
            resultContent = Jsons.prettyPrint(Jsons.toJson(result));
        }
        if (throwable != null) {
            state = ActionState.FAILED;
            resultContent = StackTraces.of(throwable);
        }

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(action);
        actionLog.setType(type);
        actionLog.setTarget(target);
        actionLog.setMethod(method);
        actionLog.setParams(params);
        actionLog.setState(state);
        actionLog.setResult(resultContent);
        actionLog.setOperator(Authentications.currentUsername());
        SessionDetails sessionDetails = Sessions.ofCurrentDetails();
        if (sessionDetails != null) {
            actionLog.setIp(sessionDetails.getIp());
            actionLog.setLocation(sessionDetails.getLocation());
            actionLog.setDevice(sessionDetails.getAccessType());
            repository.save(actionLog);
            return;
        }
        // 可能还没查到 IP 地址的地理位置，此时手动发起转换请求
        String host = Authentications.currentHost();
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
