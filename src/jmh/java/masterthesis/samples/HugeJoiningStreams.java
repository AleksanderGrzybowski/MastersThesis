package masterthesis.samples;

import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static masterthesis.tpc.StreamUtils.crossJoin;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class HugeJoiningStreams {
    
    private List<Integer> firstList = new ArrayList<>();
    private List<Integer> secondList = new ArrayList<>();
    
    @Param({"100", "200", "500", "1000", "2000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        for (int i = 0; i < numberCount; ++i) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            firstList.add(i);
            secondList.add(i);
        }
    }
    
    @Benchmark
    public List<Pair<Integer, Integer>> streams() throws Exception {
        return crossJoin(firstList, secondList)
                .collect(toList());
    }
}
