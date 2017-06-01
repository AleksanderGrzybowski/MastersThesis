package masterthesis.samples.sortingstrings;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mastersthesis.Utils.newDatabase;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SortingStringsMysql {
    
    private Connection connection;
    
    @Param({"100", "200", "500", "1000", "2000", "5000", "10000"})
    public int count;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase("mysql");
        connection.createStatement().execute(
                "CREATE TABLE strings (val varchar(50))"
        );
        Random random = new Random(12345L);
        
        for (int i = 0; i < count; ++i) {
            String randomString = new BigInteger(130, random).toString(32);
            connection.createStatement()
                    .execute(
                            "insert into strings values ('" 
                                    + randomString +
                                    "')"
                    );
        }
    }
    
    @Benchmark
    public List<String> sql() throws Exception {
        ResultSet resultSet = connection
                .createStatement()
                .executeQuery("SELECT val FROM strings order by val");
    
        List<String> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }
}
