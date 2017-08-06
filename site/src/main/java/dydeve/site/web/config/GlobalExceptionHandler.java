package dydeve.site.web.config;

import com.alibaba.fastjson.JSON;
import dydeve.monitor.holder.ThreadLocalHolder;
import dydeve.site.web.exception.CustomServerException;
import dydeve.site.web.handler.JsonHttpMessageConverter;
import dydeve.site.web.handler.annotation.ResponseJson;
import dydeve.site.web.vo.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@code ControllerAdvice} is typically used to define {@link ExceptionHandler @ExceptionHandler},
 * {@link InitBinder @InitBinder}, and {@link ModelAttribute @ModelAttribute}
 * methods that apply to all {@link RequestMapping @RequestMapping} methods.
 *
 * <a href="http://www.cnblogs.com/xinzhao/p/4902295.html"></a>
 * Created by dy on 2017/7/23.
 */
//todo no effect
@ControllerAdvice
public class GlobalExceptionHandler {


    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomServerException.class)
    @ResponseBody
    public String CustomServerExceptionHandler(CustomServerException e/*, HttpServletRequest request*/) {

        //todo async log and trace  ErrorController
        log.error("customerServerException happens, traceId:{}.", ThreadLocalHolder.TRACE_ID.get(), e);

        return JSON.toJSONString(ApiResponse.fail(e));
    }

    //httpmessage不支持
    /*@ExceptionHandler(Exception.class)
    @ResponseJson*/
    public Object CustomServerExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //todo async log and trace  ErrorController
        log.error("customerServerException happens, traceId:{}.", ThreadLocalHolder.TRACE_ID.get(), e);

        /*JsonHttpMessageConverter a = new JsonHttpMessageConverter();
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        a.write(ApiResponse.fail(new CustomServerException(111)),null, outputMessage);*/
        return ApiResponse.fail(new CustomServerException(111));
    }


}
