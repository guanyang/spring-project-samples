package org.gy.demo.config;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mysqlclient.MySQLException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.util.Result;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Uni<RestResponse<Result<Void>>> exception(Exception e, HttpServerRequest request) {
        log.error("Default Exception: uri={}", request.uri(), e);
        return Uni.createFrom().item(() -> RestResponse.ok(Result.error(Response.Status.INTERNAL_SERVER_ERROR, "系统异常，请稍后再试")));
    }

    @ServerExceptionMapper
    public Uni<RestResponse<Result<Void>>> mysqlException(MySQLException e, HttpServerRequest request) {
        log.warn("MySQLException: uri={}", request.uri(), e);
        return Uni.createFrom().item(() -> {
            String msg = e.getErrorCode() == 1062 ? "数据库记录已存在" : "数据库操作异常";
            return RestResponse.ok(Result.error(Response.Status.INTERNAL_SERVER_ERROR, msg));
        });
    }

    @ServerExceptionMapper
    public Uni<RestResponse<Result<Void>>> notSupportedException(NotSupportedException e, HttpServerRequest request) {
        log.info("NotSupportedException: uri={}", request.uri(), e);
        return Uni.createFrom().item(() -> RestResponse.ok(Result.error(Response.Status.UNSUPPORTED_MEDIA_TYPE, e.getMessage())));
    }

    @ServerExceptionMapper
    public Uni<RestResponse<Result<Void>>> constraintViolationException(ConstraintViolationException e, HttpServerRequest request) {
        log.info("ConstraintViolationException: uri={}", request.uri(), e);
        return Uni.createFrom().item(() -> RestResponse.ok(Result.error(Response.Status.BAD_REQUEST, buildMessage(e.getConstraintViolations()))));
    }

    private static String buildMessage(Set<ConstraintViolation<?>> violations) {
        return Optional.ofNullable(violations).map(Set::iterator).map(Iterator::next).map(ConstraintViolation::getMessage).orElseGet(() -> "参数错误");
    }

}
