package dydeve.monitor.holder;

import dydeve.monitor.stat.MapStatFactory;

/**
 * singleton by inner class
 * Created by dy on 2017/8/2.
 */
public class SingletonHolder {

    public static final MapStatFactory getMapStatFactory() {
        return Inner.mapStatFactory;
    }

    static class Inner {
        static final MapStatFactory mapStatFactory = new MapStatFactory();
    }

}
