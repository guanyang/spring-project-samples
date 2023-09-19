package org.gy.demo.dubbo.provider.filter;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;
import static org.apache.dubbo.common.constants.FilterConstants.VALIDATION_KEY;
import static org.gy.demo.dubbo.api.dto.support.BaseErrorCode.PARAM_ERROR;

/**
 * 功能描述：自定义验证器
 *
 * @author gy
 * @version 1.0.0
 * @date 2022/11/23 19:05
 */
@Activate(group = {CONSUMER, PROVIDER}, value = "customValidationFilter", order = 10000)
public class CustomValidationFilter implements Filter {

    private Validation validation;

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (validation != null && !invocation.getMethodName().startsWith("$") && ConfigUtils.isNotEmpty(invoker.getUrl().getMethodParameter(invocation.getMethodName(), VALIDATION_KEY))) {
            try {
                Validator validator = validation.getValidator(invoker.getUrl());
                if (validator != null) {
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }
            } catch (ConstraintViolationException e) {
                ConstraintViolation<?> constraintViolation = ((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get();
                return AsyncRpcResult.newDefaultAsyncResult(org.gy.demo.dubbo.api.dto.support.Result.error(PARAM_ERROR, constraintViolation.getMessage()), invocation);
            } catch (RpcException e) {
                throw e;
            } catch (ValidationException e) {
                // only use exception's message to avoid potential serialization issue
                return AsyncRpcResult.newDefaultAsyncResult(new ValidationException(e.getMessage()), invocation);
            } catch (Throwable t) {
                return AsyncRpcResult.newDefaultAsyncResult(t, invocation);
            }
        }
        return invoker.invoke(invocation);
    }
}
