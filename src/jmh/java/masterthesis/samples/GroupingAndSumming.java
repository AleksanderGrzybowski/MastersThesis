package masterthesis.samples;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    @Param({"1000", "10000", "100000"})
    public int numberCount;
    
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
    public Map<Integer, Integer> loop() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            result.put(i, 0);
        }
        for (Pair<Integer, Integer> pair : pairs) {
            result.put(pair.getRight(), result.get(pair.getRight()) + pair.getLeft());
        }
        return result;
    }
    
    @Benchmark
    public Map<Integer, Integer> sql() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery("select sum(val1), val2 from pairs group by val2");
        
        Map<Integer, Integer> result = new HashMap<>();
        while (rs.next()) {
            result.put(rs.getInt(2), rs.getInt(1));
        }
        return result;
    }
    
    @Benchmark
    public Map<Integer, Integer> streams() throws Exception {
        return pairs.stream().collect(Collectors.groupingBy(Pair::getRight, Collectors.summingInt(Pair::getLeft)));
    }
}
