package dydeve.web.vo;

import com.dydeve.web.exception.CustomServerException;
import org.springframework.http.HttpStatus;

/**
 * common api response
 * Created by dy on 2017/7/19.
 */
public class ApiResponse {

    private boolean success;
    private int code;
    private String msg;
    private Object data;

    public static final ApiResponse EMPTY_RESPONSE = success(null);

    public static ApiResponse success(Object data) {

        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setCode(HttpStatus.OK.value());
        response.setMsg(HttpStatus.OK.getReasonPhrase());
        response.setData(data);

        return response;
    }

    public static ApiResponse fail(CustomServerException e) {

        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setCode(e.getCode());
        response.setMsg(e.getMessage());

        return response;

    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
