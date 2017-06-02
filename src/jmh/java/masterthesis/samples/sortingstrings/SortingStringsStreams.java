package masterthesis.samples.sortingstrings;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Looping over array is faster than list, but this makes perfect sense.
 * Sequential streams are fast for small datasets, but for larger, parallel win.
 * SQL is much slower, due to data structure overhead, when we just have some numbers to sum.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SortingStringsStreams {
    
    private List<String> strings = new ArrayList<>();
    private String[] stringsArray;
    
    @Param({"100", "200", "500", "1000", "1500", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000"})
    public int count;
    
    @Setup
    public void setup() throws Exception {
        Random random = new Random(12345L);
        
        for (int i = 0; i < count; ++i) {
            strings.add(new BigInteger(130, random).toString(32));
        }
        stringsArray = strings.toArray(new String[strings.size()]);
    }
    
    @Benchmark
    public String[] arrays_sort() throws Exception {
        String[] copy = stringsArray.clone();
        Arrays.sort(copy);
        return copy;
    }
    
    @Benchmark
    public List<String> list_sort() throws Exception {
        List<String> copy = new ArrayList<>(strings);
        Collections.sort(copy);
        return copy;
    }
    
    @Benchmark
    public List<String> stream() throws Exception {
        return strings.stream().sorted().collect(toList());
    }
    
    @Benchmark
    public List<String> parallelStream() throws Exception {
        return strings.parallelStream().sorted().collect(toList());
    }
}
