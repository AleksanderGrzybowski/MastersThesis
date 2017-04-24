package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Arrays are the fastest, then lists.
 * As in SummingIntegers, sequential streams are better for small sets, parallel for bigger.
 * SQL is again way too slow.
 */
@SuppressWarnings({"SqlResolve", "SqlDialectInspection"})
@State(Scope.Thread)
public class FilteringAndCountingIntegersStreams {
    
    private List<Integer> numbers = new ArrayList<>();
    private int[] numbersArray;
    
    @Param({"100", "1000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        Random random = new Random(12345L);
        
        for (int i = 0; i < numberCount; ++i) {
            int rnd = random.nextInt();
            numbers.add(rnd);
        }
        numbersArray = numbers.stream().mapToInt(i -> i).toArray();
    }
    
    @SuppressWarnings("Convert2streamapi")
    @Benchmark
    public int loop_array() throws Exception {
        int count = 0;
        for (int i : numbersArray) {
            if (i > 0) {
                count++;
            }
        }
        return count;
    }
    
    @SuppressWarnings("Convert2streamapi")
    @Benchmark
    public int loop_list() throws Exception {
        int count = 0;
        for (int i : numbers) {
            if (i > 0) {
                count++;
            }
        }
        return count;
    }
    
    @Benchmark
    public int streams() throws Exception {
        return (int) numbers.stream().filter(i -> i > 0).count();
    }
    
    @Benchmark
    public int parallelStreams() throws Exception {
        return (int) numbers.parallelStream().filter(i -> i > 0).count();
    }
}
