package tech.yeez.investment.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.yeez.investment.controller.SubscriberController;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: globalException handle
 * @author: xiangbin
 * @create: 2022-03-17 10:43
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("[GlobalExceptionHandler]url:{}e:", request.getRequestURL(), e);
        Result result = new Result();
        result.setResultCode(ResultDesc.ERROR.getResultCode());
        result.setResultDesc(ResultDesc.ERROR.getResultDesc());
        return result;
    }
}
