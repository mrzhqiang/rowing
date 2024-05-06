package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.github.mrzhqiang.rowing.domain.ActionState;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.exception.ExceptionCauses;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.session.SessionDetails;
import com.github.mrzhqiang.rowing.session.SessionDetailsHandler;
import com.github.mrzhqiang.rowing.session.SessionDetailsMapper;
import com.github.mrzhqiang.rowing.session.Sessions;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.github.mrzhqiang.rowing.util.Jsons;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * 操作切面。
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActionAspect {

    private static final String LOCALHOST_ADDRESS = "127.0.0.1";
    private static final String LOCALHOST_DOMAIN = "localhost";
    private static final String PARAMETERS_TEMPLATE = "%s=%s";
    private static final String NO_CONTENT = "(void)";
    private static final List<String> LOCALHOST_LIST = ImmutableList.of(
            LOCALHOST_ADDRESS, LOCALHOST_DOMAIN
    );

    private final ActionLogRepository repository;
    private final SessionDetailsHandler sessionDetailsHandler;
    private final SessionDetailsMapper sessionDetailsMapper;

    @Around("@annotation(action)")
    public Object actionMethod(ProceedingJoinPoint point, Action action) throws Throwable {
        ActionType type = Optional.ofNullable(action).map(Action::value).orElse(ActionType.NONE);

        Class<?> targetClass = point.getTarget().getClass();
        String target = Optional.ofNullable(targetClass.getCanonicalName()).orElseGet(targetClass::getSimpleName);
        target = target.substring(0, Math.min(Domains.CLASS_NAME_LENGTH, target.length()));
        Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
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
        String params = Joiner.on(CommonSymbols.HALF_COMMA).join(paramList);
        if (StringUtils.hasLength(params)) {
            params = params.substring(0, Math.min(Domains.METHOD_PARAMS_LENGTH, params.length()));
        }

        ActionLog actionLog = new ActionLog();
        actionLog.setType(type);
        actionLog.setTarget(target);
        actionLog.setMethod(method);
        actionLog.setParams(params);
        actionLog.setState(ActionState.UNKNOWN);
        actionLog.setResult(NO_CONTENT);

        boolean stopWhenCompleted = CompletionStage.class.isAssignableFrom(targetMethod.getReturnType());
        if (stopWhenCompleted) {
            try {
                return ((CompletionStage<?>) point.proceed()).whenComplete((result, throwable) -> markState(actionLog, throwable));
            } catch (Throwable e) {
                markState(actionLog, e);
                throw e;
            }
        }

        try {
            Object result = point.proceed();
            markState(actionLog, result);
            appendActionLog(actionLog);
            return result;
        } catch (Throwable e) {
            markState(actionLog, e);
            appendActionLog(actionLog);
            throw e;
        }
    }

    private void markState(ActionLog actionLog, Throwable throwable) {
        actionLog.setState(ActionState.FAILED);
        actionLog.setResult(ExceptionCauses.findMessage(throwable));
    }

    private void markState(ActionLog actionLog, Object result) {
        actionLog.setState(ActionState.PASSING);
        actionLog.setResult(Jsons.prettyPrint(Jsons.toJson(result)));
    }

    private void appendActionLog(ActionLog log) {
        SessionDetails details = Sessions.ofCurrentDetails().orElseGet(this::findSessionDetails);
        if (details != null) {
            log.setIp(details.getIp());
            log.setLocation(details.getLocation());
            log.setDevice(details.getAccessType());
        }
        repository.save(log);
    }

    private SessionDetails findSessionDetails() {
        String host = Authentications.currentHost();
        if (LOCALHOST_LIST.contains(host)) {
            String location = I18nHolder.getAccessor().getMessage(
                    "ActionAspect.localAddress", "本机地址");
            String accessType = I18nHolder.getAccessor().getMessage(
                    "ActionAspect.localDevice", "本地机器");
            return sessionDetailsMapper.toDetails(host, location, accessType);
        }

        if (!Authentications.UNKNOWN_HOST.equals(host)) {
            try {
                return sessionDetailsHandler.findBy(host);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}
