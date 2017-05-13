package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Looping over array is faster than list, but this makes perfect sense.
 * Sequential streams are fast for small datasets, but for larger, parallel win.
 * SQL is much slower, due to data structure overhead, when we just have some numbers to sum.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SummingIntegersStreams {
    
    private List<Integer> numbers = new ArrayList<>();
    private int[] numbersArray;
    
    @Param({"100", "200", "500", "1000", "2000", "5000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        Random random = new Random(12345L);
        
        for (int i = 0; i < numberCount; ++i) {
            numbers.add(random.nextInt(1000));
        }
        numbersArray = numbers.stream().mapToInt(i -> i).toArray();
    }
    
    @Benchmark
    public int loop_over_list() throws Exception {
        int sum = 0;
        for (int i : numbers) {
            sum += i;
        }
        return sum;
    }
    
    @Benchmark
    public int loop_over_array() throws Exception {
        int sum = 0;
        for (int i : numbersArray) {
            sum += i;
        }
        return sum;
    }
    
    @Benchmark
    public int stream() throws Exception {
        return numbers.stream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public int parallelStream() throws Exception {
        return numbers.parallelStream().mapToInt(i -> i).sum();
    }
}
