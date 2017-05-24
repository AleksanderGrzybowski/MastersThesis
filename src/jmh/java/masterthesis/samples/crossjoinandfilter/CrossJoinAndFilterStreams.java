package masterthesis.samples.crossjoinandfilter;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static masterthesis.tpc.StreamUtils.crossJoin;

/**
 * Sequential streams are almost the same as parallel.
 * SQL is slower, but only 4x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class CrossJoinAndFilterStreams {
    
    private List<Pair<Integer, Integer>> firstList = new ArrayList<>();
    private List<Pair<Integer, String>> secondList = new ArrayList<>();
    
    @Param({"100", "200", "500", "1000", "2000", "5000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        for (int i = 0; i < 5; ++i) {
            secondList.add(new ImmutablePair<>(i, "hello" + i));
        }
        
        for (int i = 0; i < numberCount; ++i) {
            firstList.add(new ImmutablePair<>(i, i % 5));
        }
    }
    
    @Benchmark
    public List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> streams() throws Exception {
        return crossJoin(firstList, secondList)
                .filter(e -> Objects.equals(e.getLeft().getRight(), e.getRight().getLeft()))
                .distinct()
                .collect(toList());
    }
    
    @Benchmark
    public List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> parallelStreams() throws Exception {
        return crossJoin(firstList, secondList).parallel()
                .filter(e -> Objects.equals(e.getLeft().getRight(), e.getRight().getLeft()))
                .distinct()
                .collect(toList());
    }
}
