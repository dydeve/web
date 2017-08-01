package dydeve.monitor.stat;

/**
 * Created by dy on 2017/8/1.
 */
public interface IStatsFactory<S extends IStats<?>> {

    /**
     * create stats
     * @return
     */
    S createStats();

}
