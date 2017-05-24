package masterthesis.tpc.tpc6;

import mastersthesis.Utils;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;

import static mastersthesis.Utils.createSchemaMysql;

/**
 * SQL is 2-3x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc6BenchmarkMysql {
    
    Connection connection;
    
    @Param({"0.01", "0.02", "0.05", "0.1", "0.2"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase("mysql");
        createSchemaMysql(connection);
    }
    
    @Benchmark
    public BigDecimal sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
                "select\n" +
                        "sum(l_extendedprice * l_discount) as revenue\n" +
                        "from\n" +
                        "LINEITEM\n" +
                        "where\n" +
                        "l_shipdate >= date '1994-01-01'\n" +
                        "and l_shipdate < date '1995-01-01'" +
                        "and l_discount between 0.06 - 0.01 and 0.06 + 0.01\n" +
                        "and l_quantity < 24;\n"
        ).executeQuery();
        rs.next();
        
        BigDecimal result = rs.getBigDecimal(1);
        System.out.println(result);
        return result;
    }
    
}

