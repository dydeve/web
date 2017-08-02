package dydeve.monitor.stat;

/**
 * Created by dy on 2017/8/1.
 */
public interface IStatFactory<E, T, S extends IStat<E, T>> {

    /**
     * create stats
     * @return
     */
    S createStat();

}
