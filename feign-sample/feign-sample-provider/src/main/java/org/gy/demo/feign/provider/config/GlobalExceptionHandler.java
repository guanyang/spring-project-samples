package org.gy.demo.feign.provider.config;


import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.feign.api.dto.support.Result;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static cn.hutool.http.HttpStatus.HTTP_BAD_REQUEST;
import static org.gy.demo.feign.provider.config.DefaultExceptionHandler.handleResult;

/**
 * @author guanyang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(HttpServletRequest request, BindException e) {
        String msg = buildErrMsg(e);
        return handleResult(request, e, "参数类型错误", HTTP_BAD_REQUEST, msg, LogLevel.WARN);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<Void> handleException(HttpServletRequest request, ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        String msg = constraintViolations.stream().findFirst().map(item -> String.format("【%s】%s", item.getPropertyPath(), item.getMessage())).orElseGet(() -> "Request parameter format error");
        return handleResult(request, ex, "ConstraintViolation fail", HTTP_BAD_REQUEST, msg, LogLevel.WARN);
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public Result<Void> handleException(HttpServletRequest request, HandlerMethodValidationException e) {
        String msg = buildErrMsg(e.getAllErrors());
        return handleResult(request, e, "参数验证错误", HTTP_BAD_REQUEST, msg, LogLevel.WARN);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result<Void> handleException(HttpServletRequest request, HttpMessageNotReadableException e) {
        String msg = buildErrMsg(e);
        return handleResult(request, e, "参数格式错误", HTTP_BAD_REQUEST, msg, LogLevel.WARN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleException(HttpServletRequest request, IllegalArgumentException e) {
        return handleResult(request, e, "参数错误", HTTP_BAD_REQUEST, e.getMessage(), LogLevel.WARN);
    }

    private String buildErrMsg(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            String fieldName = Optional.ofNullable(((InvalidFormatException) cause).getPath()).flatMap(list -> list.stream().findFirst()).map(Reference::getFieldName).orElseGet(() -> "Unknown");
            return String.format("【%s】%s", fieldName, "参数格式转换异常");
        } else {
            return cause.getMessage();
        }
    }

    private String buildErrMsg(BindingResult br) {
        return Optional.ofNullable(br).map(BindingResult::getFieldError).map(fieldError -> {
            String msg = Optional.ofNullable(fieldError.getDefaultMessage()).filter(s -> s.length() < 80).orElseGet(() -> "参数数据类型转换异常");
            return String.format("【%s】%s", fieldError.getField(), msg);
        }).orElseGet(() -> "请求参数格式错误");
    }

    private String buildErrMsg(List<? extends MessageSourceResolvable> allErrors) {
        List<? extends MessageSourceResolvable> errList = Optional.ofNullable(allErrors).orElseGet(Collections::emptyList);
        String msg = errList.stream().findFirst().map(MessageSourceResolvable::getDefaultMessage).orElseGet(() -> "参数验证失败");
        return String.format("Request parameter error: %s", msg);
    }
}

