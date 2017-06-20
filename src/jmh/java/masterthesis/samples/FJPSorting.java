package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class FJPSorting {
    
    @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "20","25", "30"})
    public int poolSize;
    
    List<Integer> numbers = new ArrayList<>();
    ForkJoinPool pool;
    
    @Setup
    public void setup() throws Exception {
        numbers = new Random(12345L).ints().boxed().limit(1000000).collect(toList());
        pool = new ForkJoinPool(poolSize);
    }
    
    @Benchmark
    public Object test() throws ExecutionException, InterruptedException {
        return pool.submit(
                () -> numbers.parallelStream().sorted().collect(toList())
        ).get();
    }
}
