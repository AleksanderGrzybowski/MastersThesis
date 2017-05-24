package masterthesis.samples.groupingandsumming;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parallel faster for bigger datasets.
 * SQL 10x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class GroupingAndSummingStreams {
    
    private List<Pair<Integer, Integer>> pairs = new ArrayList<>();
    
    @Param({"100", "200", "500", "1000", "2000", "5000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        for (int i = 0; i < numberCount; ++i) {
            pairs.add(new ImmutablePair<>(i, i % 5));
        }
    }
    
    @Benchmark
    public Map<Integer, Integer> loop() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            result.put(i, 0);
        }
        for (Pair<Integer, Integer> pair : pairs) {
            result.put(pair.getRight(), result.get(pair.getRight()) + pair.getLeft());
        }
        return result;
    }
    
    @Benchmark
    public Map<Integer, Integer> streams() throws Exception {
        return pairs.stream().collect(Collectors.groupingBy(Pair::getRight, Collectors.summingInt(Pair::getLeft)));
    }
    
    @Benchmark
    public Map<Integer, Integer> parallelStreams() throws Exception {
        return pairs.parallelStream().collect(Collectors.groupingBy(Pair::getRight, Collectors.summingInt(Pair::getLeft)));
    }
}
