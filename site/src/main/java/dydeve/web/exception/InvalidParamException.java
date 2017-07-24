package dydeve.web.exception;

/**
 * Created by yuduy on 2017/7/21.
 */
public class InvalidParamException extends CustomServerException {
    public InvalidParamException(String msg, int code) {
        super(msg, code);
    }
}
