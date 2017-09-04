package dydeve.db.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yuduy on 2017/9/4.
 */
public class WeightedRobinLoadBalanceStrategy<T> implements LoadBalanceStrategy<T> {
    @Override
    public T chose() {
        return null;
    }

    public WeightedRobinLoadBalanceStrategy(Map<T, Integer> weightedSources) {
        if (weightedSources == null || weightedSources.size() <= 1) {
            throw new IllegalArgumentException("sources's or weights' size must bigger than  1");
        }
        int maxWeightGcd = maxGcd(weightedSources.values().toArray(new Integer[]{}));
        int maxWeight = maxWeight(weightedSources.values().toArray(new Integer[]{}));
        int sourceNum = weightedSources.size();

    }

    public int maxGcd(Integer[] weights) {

        int rtn = 0;
        for (int i = 0; i < weights.length; i++) {
            if (i == 0) {
                rtn = gcd(weights[i], weights[++ i]);
            } else {
                rtn = gcd(rtn, weights[i]);
            }
        }
        return rtn;

    }

    public int maxWeight(Integer[] weights) {
        int max = weights[0];
        for (int i = 1; i < weights.length; i++) {
            max = Math.max(max, weights[i]);
        }
        return max;
    }


    /**
     * 最大公约数
     */
    public static int gcd(int a, int b) {
        if (a % b == 0) {
            return b;
        }
        return gcd(b, a % b);
    }
}
