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

/**
 * Sequential streams are almost the same as parallel.
 * SQL is slower, but only 4x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class CrossJoinAndFilterH2 {
    
    private Connection connection;
    
    @Param({"100", "1000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase("h2");
        connection.createStatement().execute("CREATE TABLE firstList (val1 INT, val2 INT)");
        connection.createStatement().execute("CREATE TABLE secondList (val1 INT PRIMARY KEY, val2 VARCHAR(10))");
        connection.createStatement().execute(
                "ALTER TABLE firstList ADD FOREIGN KEY (val2) REFERENCES secondList(val1)"
        );
        
        for (int i = 0; i < 5; ++i) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO secondList VALUES (?, ?)");
            statement.setInt(1, i);
            statement.setString(2, "hello" + i);
            statement.execute();
        }
        
        for (int i = 0; i < numberCount; ++i) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO firstList VALUES (?, ?)");
            statement.setInt(1, i);
            statement.setInt(2, i % 5);
            statement.execute();
        }
    }
    
    @Benchmark
    public List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> sql() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM firstList JOIN secondList ON firstList.val2 = secondList.val1"
        );
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new ImmutablePair<>(
                    new ImmutablePair<>(rs.getInt(1), rs.getInt(2)),
                    new ImmutablePair<>(rs.getInt(3), rs.getString(4))
            ));
        }
        return result;
    }
}
