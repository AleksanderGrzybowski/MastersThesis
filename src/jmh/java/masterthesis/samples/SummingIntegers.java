package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mastersthesis.Utils.newDatabase;

/**
 * Looping over array is faster than list, but this makes perfect sense.
 * Sequential streams are fast for small datasets, but for larger, parallel win.
 * SQL is much slower, due to data structure overhead, when we just have some numbers to sum.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SummingIntegers {
    
    private Connection connection;
    private List<Integer> numbers = new ArrayList<>();
    private int[] numbersArray;
    
    @Param({"1000", "10000", "100000", "1000000"})
    public int numberCount;
    
    @Param({"h2", "mysql"})
    public String databaseVendor;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase(databaseVendor);
        connection.createStatement().execute("CREATE TABLE numbers (val INT)");
        Random random = new Random();
        
        for (int i = 0; i < numberCount; ++i) {
            int randomNumber = random.nextInt(1000);
            numbers.add(randomNumber);
            connection.createStatement().execute("insert into numbers values (" + randomNumber + ")");
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
    public int sql() throws Exception {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT sum(val) FROM numbers");
        resultSet.next();
        
        return resultSet.getInt(1);
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
