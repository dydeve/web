package dydeve.monitor.stat;

/**
 * Created by dy on 2017/8/1.
 */
public class MapStatsFactory implements IStatsFactory<MapStats> {
    @Override
    public MapStats createStats() {
        return new MapStats();
    }
}
