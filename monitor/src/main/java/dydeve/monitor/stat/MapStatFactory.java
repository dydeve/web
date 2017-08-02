package dydeve.monitor.stat;

import java.util.Map;

/**
 * Created by dy on 2017/8/1.
 */
public class MapStatFactory implements IStatFactory<Map.Entry<String, Object>, String, MapStat> {
    @Override
    public MapStat createStat() {
        return new MapStat();
    }
}
