package org.gy.demo.mq.event.trace;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * @author gy
 */
public class TraceFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String traceId = httpServletRequest.getHeader(TraceEnum.TRACE.getName());
        if (StrUtil.isNotBlank(traceId)) {
            TraceContext.setTrace(traceId);
        } else {
            TraceContext.getTrace();
        }
        try {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } finally {
            TraceContext.clearTrace();
        }
    }
}
