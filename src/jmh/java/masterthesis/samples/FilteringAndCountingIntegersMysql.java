package masterthesis.samples;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.joining;
import static mastersthesis.Utils.newDatabase;

/**
 * Arrays are the fastest, then lists.
 * As in SummingIntegers, sequential streams are better for small sets, parallel for bigger.
 * SQL is again way too slow.
 */
@SuppressWarnings({"SqlResolve", "SqlDialectInspection"})
@State(Scope.Thread)
public class FilteringAndCountingIntegersMysql {
    
    private Connection connection;
    
    @Param({"100", "1000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        Random random = new Random(12345L);
        connection = newDatabase("mysql");
        connection.createStatement().execute("create table numbers (val int)");
        
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < numberCount; ++i) {
            int rnd = random.nextInt();
            numbers.add(rnd);
        }
        connection.createStatement().execute(
                "insert into numbers values " + numbers.stream().map(i -> "(" + i + ")").collect(joining(","))
        
        );
    }
    
    @Benchmark
    public int sql() throws Exception {
        ResultSet resultSet = connection.createStatement().executeQuery(
                "select count(*) from numbers where val > 0"
        );
        resultSet.next();
        
        return resultSet.getInt(1);
    }
}
