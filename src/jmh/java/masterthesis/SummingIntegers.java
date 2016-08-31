package masterthesis;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Main.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SummingIntegers {
    
    private Connection connection;
    private List<Integer> numbers = new ArrayList<>();
    private int[] numbersArray;
    
    @Param({"1000000"})
    private int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase();
        connection.createStatement().execute("CREATE TABLE numbers (val INT)");
        
        for (int i = 0; i < numberCount; ++i) {
            numbers.add(i);
            connection.createStatement().execute("insert into numbers values (" + i + ")");
        }
        numbersArray = numbers.stream().mapToInt(i -> i).toArray();
    }
    
    @Benchmark
    public Object loop_over_list() throws Exception {
        int sum = 0;
        for (int i : numbers) {
            sum += i;
        }
        return sum;
    }
    
    @Benchmark
    public Object loop_over_array() throws Exception {
        int sum = 0;
        for (int i : numbersArray) {
            sum += i;
        }
        return sum;
    }
    
    @Benchmark
    public Object sql() throws Exception {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT sum(val) FROM numbers");
        resultSet.next();
        
        return resultSet.getObject(1);
    }
    
    @Benchmark
    public Object stream() throws Exception {
        return numbers.stream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public Object parallelStream() throws Exception {
        return numbers.parallelStream().mapToInt(i -> i).sum();
    }
}
