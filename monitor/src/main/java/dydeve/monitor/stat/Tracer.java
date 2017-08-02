package dydeve.monitor.stat;

import dydeve.monitor.common.NotThreadSafe;
import dydeve.monitor.util.IntegerSequencer;
import dydeve.monitor.util.Sequencer;
import dydeve.monitor.util.Sequential;

import java.util.LinkedList;

/**
 * single thread tracer.
 * not thread safe
 * Created by dy on 2017/8/2.
 */
@NotThreadSafe
public class Tracer<E, T, S extends IStat<E, T>> implements Sequential<Integer>{

    private Group group;//group identify, like topic in kafka

    private String traceId;

    private IStatFactory<E, T, S> statFactory;

    private LinkedList<S> stats;

    private Sequencer<Integer> counter;

    public Tracer(Group group, String traceId, IStatFactory<E, T, S> statFactory) {
        this.group = group;
        this.traceId = traceId;
        this.statFactory = statFactory;
        this.stats = new LinkedList<>();
        this.counter = IntegerSequencer.newInstance();
    }

    public S makeStat() {
        S stat = statFactory.createStat();
        stat.setAttribute(IStat.sequence, sequencer().getAndNext());
        stats.add(stat);
        return stat;
    }

    public S popStat() {
        if (stats.isEmpty()) {
            return null;
        }
        return stats.pop();
    }

    @Override
    public Sequencer<Integer> sequencer() {
        /*if (counter != null) {
            return counter;
        }
        counter = IntegerSequencer.newInstance();*/
        return counter;
    }

    public enum Group {
        SITE,
        PAY,
        ELK,
        FLUME,
        STORM,
        SPARK;
    }

}
