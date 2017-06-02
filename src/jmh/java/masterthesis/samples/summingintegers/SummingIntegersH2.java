package masterthesis.samples.summingintegers;

import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Random;

import static mastersthesis.Utils.newDatabase;

/**
 * Looping over array is faster than list, but this makes perfect sense.
 * Sequential streams are fast for small datasets, but for larger, parallel win.
 * SQL is much slower, due to data structure overhead, when we just have some numbers to sum.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class SummingIntegersH2 {
    
    private Connection connection;
    
    @Param({"100", "200", "500", "1000", "1500", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000"})
    public int numberCount;
    
    @Setup
    public void setup() throws Exception {
        connection = newDatabase("h2");
        connection.createStatement().execute(
                "CREATE TABLE numbers (val INT)"
        );
        Random random = new Random(12345L);
        
        for (int i = 0; i < numberCount; ++i) {
            int randomNumber = random.nextInt(1000);
            connection.createStatement()
                    .execute(
                            "insert into numbers values ("
                                    + randomNumber +
                                    ")"
                    );
        }
    }
    
    @Benchmark
    public int sql() throws Exception {
        ResultSet resultSet = connection
                .createStatement()
                .executeQuery("SELECT sum(val) FROM numbers");
        resultSet.next();
        
        return resultSet.getInt(1);
    }
}
