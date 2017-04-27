package masterthesis.samples;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class HugeJoiningH2 {
    
    private Connection connection;
    
    @Param({"2000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase("h2");
        connection.createStatement().execute("CREATE TABLE firstList (val1 INT)");
        connection.createStatement().execute("CREATE TABLE secondList (val2 INT)");
        
        for (int i = 0; i < numberCount; ++i) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            PreparedStatement statement = connection.prepareStatement("INSERT INTO firstList VALUES (?)");
            statement.setInt(1, i);
            statement.execute();
            statement = connection.prepareStatement("INSERT INTO secondList VALUES (?)");
            statement.setInt(1, i);
            statement.execute();
        }
    }
    
    @Benchmark
    public List<Pair<Integer, Integer>> sql() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM firstList, secondList order by val1, val2"
        );
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new ImmutablePair<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
    }
}
