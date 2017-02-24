package masterthesis.samples;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static mastersthesis.Utils.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class GroupingAndSumming {
    
    private Connection connection;
    private List<Pair<Integer, Integer>> pairs = new ArrayList<>();
    
    @Param({"10000"})
    private int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase();
        connection.createStatement().execute("create table pairs (val1 int, val2 int)");
        
        for (int i = 0; i < numberCount; ++i) {
            pairs.add(new ImmutablePair<>(i, i % 5));
            PreparedStatement statement = connection.prepareStatement("insert into pairs values (?, ?)");
            statement.setInt(1, i);
            statement.setInt(2, i % 5);
            statement.execute();
        }
    }
    
    @Benchmark
    public Object loop() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i <= 5; i++) {
            result.put(i, 0);
        }
        for (Pair<Integer, Integer> pair : pairs) {
            result.put(pair.getRight(), result.get(pair.getRight()) + pair.getLeft());
        }
        return result;
    }
    
    @Benchmark
    public Object sql() throws Exception {
        return connection.createStatement().executeQuery("select sum(val1), val2 from pairs group by val2");
    }
    
    @Benchmark
    public Object streams() throws Exception {
        return pairs.stream().collect(Collectors.groupingBy(Pair::getValue));
    }
}
