package masterthesis.samples.groupingandsumming;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static mastersthesis.Utils.newDatabase;

/**
 * Parallel faster for bigger datasets.
 * SQL 10x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class GroupingAndSummingH2 {
    
    private Connection connection;
    
    @Param({"100", "200", "500", "1000", "1500", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase("h2");
        connection.createStatement().execute("create table pairs (val1 int, val2 int)");
        
        for (int i = 0; i < numberCount; ++i) {
            PreparedStatement statement = connection.prepareStatement("insert into pairs values (?, ?)");
            statement.setInt(1, i);
            statement.setInt(2, i % 5);
            statement.execute();
        }
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
}
