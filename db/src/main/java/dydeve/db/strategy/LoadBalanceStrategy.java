package dydeve.db.strategy;

/**
 * Created by dy on 2017/8/29.
 */
public interface LoadBalanceStrategy<T> {

    T chose();

}
