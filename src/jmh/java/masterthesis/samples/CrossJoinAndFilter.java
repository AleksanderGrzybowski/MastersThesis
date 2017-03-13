package masterthesis.samples;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static mastersthesis.Utils.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class CrossJoinAndFilter {
    
    private Connection connection;
    private List<Pair<Integer, Integer>> firstList = new ArrayList<>();
    private List<Pair<Integer, String>> secondList = new ArrayList<>();
    
    @Param({"300000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase();
        connection.createStatement().execute("CREATE TABLE firstList (val1 INT, val2 INT)");
        connection.createStatement().execute("CREATE TABLE secondList (val1 INT PRIMARY KEY, val2 VARCHAR(10))");
        connection.createStatement().execute(
                "ALTER TABLE firstList ADD FOREIGN KEY (val2) REFERENCES secondList(val1)"
        );
        
        for (int i = 0; i < 5; ++i) {
            secondList.add(new ImmutablePair<>(i, "hello" + i));
            PreparedStatement statement = connection.prepareStatement("INSERT INTO secondList VALUES (?, ?)");
            statement.setInt(1, i);
            statement.setString(2, "hello" + i);
            statement.execute();
        }
        
        for (int i = 0; i < numberCount; ++i) {
            firstList.add(new ImmutablePair<>(i, i % 5));
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
    
    
    private <T, U> Stream<ImmutablePair<T, U>> crossJoin(List<T> first, List<U> second) {
        return first.stream().flatMap(
                firstItem -> second.stream().map(secondItem -> new ImmutablePair<>(firstItem, secondItem))
        );
    }
    
    @Benchmark
    public List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> streams() throws Exception {
        return crossJoin(firstList, secondList)
                .filter(e -> Objects.equals(e.getLeft().getRight(), e.getRight().getLeft()))
                .distinct()
                .collect(toList());
    }
}
