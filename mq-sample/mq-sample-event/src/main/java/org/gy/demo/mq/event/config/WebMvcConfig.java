package org.gy.demo.mq.event.config;

import io.github.guanyang.mq.core.TraceService;
import jakarta.annotation.Resource;
import org.gy.demo.mq.event.trace.TraceFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author gy
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer, InitializingBean {

    @Resource
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        TraceUtils.setLogTraceKey(TraceEnum.TRACE.getTraceName());
    }

    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilterRegistration() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("traceFilter");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public TraceService customTraceService() {
        return new CustomTraceService();
    }
}
