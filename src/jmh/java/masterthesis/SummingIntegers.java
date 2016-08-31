package masterthesis;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mastersthesis.Main.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SummingIntegers {
    
    private Connection connection;
    private List<Integer> numbers = new ArrayList<>();
    
    @Param({"1000000"})
    private int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase();
        connection.createStatement().execute("create table numbers (val int)");
        
        for (int i = 0; i < numberCount; ++i) {
            numbers.add(i);
            connection.createStatement().execute("insert into numbers values (" + i + ")");
        }
    }
    
    @Benchmark
    public Object loop() throws Exception {
        int sum = 0;
        for (int i: numbers) {
            sum += i;
        }
        return sum;
    }
    
    @Benchmark
    public Object sql() throws Exception {
        ResultSet resultSet = connection.createStatement().executeQuery("select sum(val) from numbers");
        resultSet.next();
    
        return resultSet.getObject(1);
    }
    
    @Benchmark
    public Object streams_mapToInt() throws Exception {
        return numbers.stream().mapToInt(i -> i).sum();
    }
    
    @Benchmark
    public Object streams_collect() throws Exception {
        return numbers.stream().collect(Collectors.summingInt(i -> i));
    }
    
}
