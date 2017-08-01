package dydeve.monitor.holder;

import dydeve.monitor.stat.IStats;
import dydeve.monitor.stat.IStatsFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dy on 2017/8/1.
 */
public class StatsHolder<S extends IStats<?>> {

    private String category;

    private String traceId;

    private List<S> stats;

    private IStatsFactory<S> factory;

    public S generateStat() {
        S stat = factory.createStats();
        this.stats.add(stat);
        return stat;
    }

    public StatsHolder(String category, String traceId, IStatsFactory<S> factory) {
        this.category = category;
        this.traceId = traceId;
        this.factory = factory;
        this.stats = new LinkedList<>();
    }

    public String getCategory() {
        return category;
    }

    public String getTraceId() {
        return traceId;
    }

    public List<S> getStats() {
        return stats;
    }
}
