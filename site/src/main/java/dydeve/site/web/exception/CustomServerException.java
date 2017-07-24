package dydeve.site.web.exception;

import com.dydeve.web.vo.AbstractResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * custom exception.
 * http code:200
 * Created by dy on 2017/7/19.
 */
public class CustomServerException extends Exception {

    private final int code;

    public CustomServerException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public CustomServerException(String msg, Throwable cause, int code) {
        super(msg, cause);
        this.code = code;
    }

    public CustomServerException(int code) {
        this.code = code;
    }

    //5**
    public CustomServerException(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.code = httpStatus.value();
    }

    //5**
    public CustomServerException(HttpStatus httpStatus, Throwable cause) {
        super(httpStatus.getReasonPhrase(), cause);
        this.code = httpStatus.value();
    }

    //code >= 600
    public CustomServerException(AbstractResponseStatus responseStatus) {
        super(responseStatus.getMsg());
        this.code = responseStatus.getCode();
    }

    //code >= 600
    public CustomServerException(AbstractResponseStatus responseStatus, Throwable cause) {
        super(responseStatus.getMsg());
        this.code = responseStatus.getCode();
    }

    public int getCode() {
        return code;
    }

}
