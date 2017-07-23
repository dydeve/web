package com.dydeve.web.config;

import com.dydeve.web.exception.CustomServerException;
import com.dydeve.web.handler.annotation.ResponseJson;
import com.dydeve.web.vo.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * {@code ControllerAdvice} is typically used to define {@link ExceptionHandler @ExceptionHandler},
 * {@link InitBinder @InitBinder}, and {@link ModelAttribute @ModelAttribute}
 * methods that apply to all {@link RequestMapping @RequestMapping} methods.
 *
 * <a href="http://www.cnblogs.com/xinzhao/p/4902295.html"></a>
 * Created by dy on 2017/7/23.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler((CustomServerException.class))
    @ResponseJson
    public ApiResponse CustomServerExceptionHandler(CustomServerException e, HttpServletRequest request) {

        //todo async log and trace  ErrorController
        log.error("customerServerException happens, request:{}", request.getRequestURI(), e);

        return ApiResponse.fail(e);
    }


}
