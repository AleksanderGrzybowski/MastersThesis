package masterthesis.samples;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static mastersthesis.Utils.newDatabase;
import static masterthesis.tpc.StreamUtils.crossJoin;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class HugeJoining {
    
    private Connection connection;
    private List<Integer> firstList = new ArrayList<>();
    private List<Integer> secondList = new ArrayList<>();
    
    @Param({"2000"})
    public int numberCount;
    
    @Param({"h2", "mysql"})
    public String databaseVendor;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase(databaseVendor);
        connection.createStatement().execute("CREATE TABLE firstList (val1 INT)");
        connection.createStatement().execute("CREATE TABLE secondList (val2 INT)");
        
        for (int i = 0; i < numberCount; ++i) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            firstList.add(i);
            secondList.add(i);
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
        System.out.println("Create statement");
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM firstList, secondList"
        );
        System.out.println("result");
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new ImmutablePair<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
    }
    
    @Benchmark
    public List<Pair<Integer, Integer>> streams() throws Exception {
        return crossJoin(firstList, secondList)
                .collect(toList());
    }
}
