package org.gy.demo.feign.provider.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.feign.api.dto.support.Result;
import org.springframework.boot.logging.LogLevel;

import java.util.function.BiFunction;

/**
 * @author guanyang
 */
@Slf4j
public class DefaultExceptionHandler {

    public static <T> Result<T> handleResult(HttpServletRequest request, Exception e, String logHolder, int code, String responseMsg, LogLevel level) {
        return handleResult(request, e, logHolder, code, responseMsg, level, Result::error);
    }

    public static <T> T handleResult(HttpServletRequest request, Exception e, String logHolder, int code, String responseMsg, LogLevel level, BiFunction<Integer, String, T> responseFunction) {
        logMessage(request, e, logHolder, responseMsg, level);
        return responseFunction.apply(code, responseMsg);
    }


    /**
     * 记录调试级别日志
     */
    public static void logDebug(HttpServletRequest request, Exception e, String logHolder, String logMsg) {
        logMessage(request, e, logHolder, logMsg, LogLevel.DEBUG);
    }

    /**
     * 记录信息级别日志
     */
    public static void logInfo(HttpServletRequest request, Exception e, String logHolder, String logMsg) {
        logMessage(request, e, logHolder, logMsg, LogLevel.INFO);
    }

    /**
     * 记录警告级别日志
     */
    public static void logWarn(HttpServletRequest request, Exception e, String logHolder, String logMsg) {
        logMessage(request, e, logHolder, logMsg, LogLevel.WARN);
    }

    /**
     * 记录错误级别日志
     */
    public static void logError(HttpServletRequest request, Exception e, String logHolder, String logMsg) {
        logMessage(request, e, logHolder, logMsg, LogLevel.ERROR);
    }

    /**
     * 根据日志级别统一处理日志记录
     */
    public static void logMessage(HttpServletRequest request, Exception e, String logHolder, String logMsg, LogLevel level) {
        String url = request.getRequestURI();
        switch (level) {
            case DEBUG:
                log.debug("{}: url={} msg={} ", logHolder, url, logMsg, e);
                break;
            case INFO:
                log.info("{}: url={} msg={} ", logHolder, url, logMsg, e);
                break;
            case ERROR:
                log.error("{}: url={} msg={} ", logHolder, url, logMsg, e);
                break;
            default:
                log.warn("{}: url={} msg={} ", logHolder, url, logMsg, e);
                break;
        }
    }
}