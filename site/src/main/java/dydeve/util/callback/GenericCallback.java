package dydeve.util.callback;

/**
 * Created by dy on 2017/7/19.
 */
@FunctionalInterface
public interface GenericCallback<P, R> extends Callback {

    R callback(P param);

}
