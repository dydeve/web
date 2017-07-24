package dydeve.web.vo;

/**
 * Created by dy on 2017/7/19.
 */
public abstract class AbstractResponseStatus implements ResponseStatus {

    private final int code;
    private final String msg;

    public AbstractResponseStatus(int code, String msg) {
        if (code <= 599) {
            throw new IllegalStateException("code must >= 600");
        }

        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
