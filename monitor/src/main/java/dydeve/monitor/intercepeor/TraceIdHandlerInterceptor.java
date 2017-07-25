package dydeve.monitor.intercepeor;

import dydeve.monitor.holder.TraceHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by yuduy on 2017/7/25.
 */
//todo 加个全局开关，控制是否开启监控发送kafaka
public class TraceIdHandlerInterceptor extends HandlerInterceptorAdapter {

    //will e invoked if last interceptor's prehandle return true,in single-thread mode or concurrent mode
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        TraceHolder.TRACE_ID.set(UUID.randomUUID().toString());
        TraceHolder.REQUEST_URI.set(request.getRequestURI());
        return true;
    }

    //invoke in normal mode,not in the case of exception happens or concurrent mode
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //resource clean work must be dong in afterCompletion
    }

    //will be invoked finally, even if exception/error happens, not in concurrent case
    //if preHandle return false, will be invoked in any case!
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //will be invoked finally, even if exception/error happens
        TraceHolder.TRACE_ID.remove();
        TraceHolder.REQUEST_URI.remove();
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        throw new UnsupportedOperationException("trace interceptor can't in concurrent mode");
    }
}
