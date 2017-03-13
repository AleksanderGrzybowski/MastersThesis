package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mastersthesis.Utils.newDatabase;

@SuppressWarnings({"SqlResolve", "SqlDialectInspection"})
@State(Scope.Thread)
public class FilteringAndCountingIntegers {
    
    private Connection connection;
    private List<Integer> numbers = new ArrayList<>();
    private int[] numbersArray;
    
    @Param({"1000000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        Random random = new Random();
        connection = newDatabase();
        connection.createStatement().execute("create table numbers (val int)");
        
        for (int i = 0; i < numberCount; ++i) {
            int rnd = random.nextInt();
            numbers.add(rnd);
            connection.createStatement().execute("insert into numbers values (" + rnd + ")");
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
    public int sql() throws Exception {
        ResultSet resultSet = connection.createStatement().executeQuery(
                "select count(*) from numbers where val > 0"
        );
        resultSet.next();
        
        return resultSet.getInt(1);
    }
    
    @Benchmark
    public int streams() throws Exception {
        return (int) numbers.stream().filter(i -> i > 0).count();
    }
}
