package dydeve.db.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dy on 2017/8/31.
 */
public class WeightedRobinLoadBalanceStrategy<T> implements LoadBalanceStrategy<T> {

    private RobinLoadBalanceStrategy<T> innerLoadBalanceStrategy;

    public WeightedRobinLoadBalanceStrategy(List<T> sources, List<Integer> weights, int cycle) {

        if (sources == null || sources.size() < 2
                || weights == null || weights.size() < 2) {
            throw new IllegalArgumentException("sources's or weights' size must bigger than  1");
        }

        if (sources.size() != weights.size()) {
            throw new IllegalArgumentException("sources' size must equal to weights' size");
        }

        this.innerLoadBalanceStrategy = new RobinLoadBalanceStrategy<>(
                distributeSources(sources, weights),
                cycle);
    }

    private List<T> distributeSources(List<T> sources, List<Integer> weights) {

        List<T> distributedSources = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(i); j++) {
                distributedSources.add(sources.get(i));
            }
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int lastIndex = distributedSources.size() - 1;



        for (int i = distributedSources.size(); i >= 2; --i) {
                                             // [0, i-1]
            swapListItem(distributedSources, random.nextInt(i), lastIndex);
        }

        if (random.nextInt() >= 0) {
            swapListItem(distributedSources, 0, lastIndex);
        }

        return distributedSources;
    }


    public static void swapListItem(List list, int index, int anotherIndex) {

        if (index == anotherIndex) {
            return;
        }

        Object tmp = list.get(index);
        list.set(index, list.get(anotherIndex));
        list.set(anotherIndex, tmp);
    }


    @Override
    public T chose() {
        return innerLoadBalanceStrategy.chose();
    }
}
