package com.github.mrzhqiang.rowing.module.exception;

import com.github.mrzhqiang.rowing.util.DateTimes;
import com.github.mrzhqiang.rowing.util.Exceptions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Mapper(componentModel = "spring", imports = {DateTimes.class, Exceptions.class, ExceptionCode.class})
public interface ExceptionLogMapper {

    @Mapping(target = "timestamp", expression = "java( DateTimes.localFormat(entity.getTimestamp()) )")
    ExceptionLogData toData(ExceptionLog entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "status", expression = "java( status.value() )")
    @Mapping(target = "error", source = "status.reasonPhrase")
    @Mapping(target = "method", source = "request.method")
    @Mapping(target = "url", expression = "java( request.getRequestURL().toString() )")
    @Mapping(target = "query", source = "request.queryString")
    @Mapping(target = "address", source = "request.remoteAddr")
    @Mapping(target = "sessionId", source = "request.requestedSessionId")
    @Mapping(target = "message", expression = "java( Exceptions.ofMessage(exception) )")
    @Mapping(target = "code", expression = "java( ExceptionCode.of(exception).format(status.value(),exception.getMessage()) )")
    @Mapping(target = "trace", expression = "java( Exceptions.ofTrace(exception) )")
    ExceptionLog toEntity(HttpStatus status, HttpServletRequest request, Exception exception);
}
