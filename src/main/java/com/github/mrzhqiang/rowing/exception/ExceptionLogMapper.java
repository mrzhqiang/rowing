package com.github.mrzhqiang.rowing.exception;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.domain.ExceptionCode;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

@Mapper(componentModel = "spring",
        imports = {Exceptions.class, ExceptionCode.class, ExceptionCauses.class})
public interface ExceptionLogMapper {

    ExceptionLogData toData(ExceptionLog entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", expression = "java( status.value() )")
    @Mapping(target = "error", source = "status.reasonPhrase")
    @Mapping(target = "method", source = "request.method")
    @Mapping(target = "url", expression = "java( request.getRequestURL().toString() )")
    @Mapping(target = "query", source = "request.queryString")
    @Mapping(target = "address", source = "request.remoteAddr")
    @Mapping(target = "sessionId", source = "request.requestedSessionId")
    @Mapping(target = "message", expression = "java( ExceptionCauses.findMessage(exception) )")
    @Mapping(target = "code", expression = "java( ExceptionCode.of(exception).format(status.value(), exception.getMessage()) )")
    @Mapping(target = "trace", expression = "java( Exceptions.ofTrace(exception) )")
    ExceptionLog toEntity(HttpStatus status, HttpServletRequest request, Exception exception);

}
