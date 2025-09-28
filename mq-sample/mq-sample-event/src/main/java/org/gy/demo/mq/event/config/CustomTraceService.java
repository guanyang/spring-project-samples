package org.gy.demo.mq.event.config;


import io.github.guanyang.mq.core.TraceService;
import org.gy.demo.mq.event.trace.TraceContext;
import org.gy.demo.mq.event.trace.TraceEnum;

/**
 * @author guanyang
 */
public class CustomTraceService implements TraceService {
    @Override
    public String getTraceKey() {
        return TraceEnum.TRACE.getName();
    }

    @Override
    public String getTraceId() {
        return TraceContext.getTraceId();
    }

    @Override
    public void setTrace(String traceId) {
        TraceContext.setTrace(traceId);
    }

    @Override
    public void clearTrace() {
        TraceContext.clearTrace();
    }
}
