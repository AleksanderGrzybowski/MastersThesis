package masterthesis.tpc.tpc14;

import mastersthesis.Utils;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;

import static mastersthesis.Utils.createSchemaMysql;

/**
 * Because of joins, SQL rocks here and streams are 50x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc14BenchmarkMysql {
    
    Connection connection;
    
    @Param({"0.010", "0.025", "0.050", "0.075", "0.100", "0.125", "0.150", "0.175", "0.200"})
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
                        "100.00 * sum(case\n" +
                        "when p_type like 'PROMO%'\n" +
                        "then l_extendedprice * (1 - l_discount)\n" +
                        "else 0\n" +
                        "end) / sum(l_extendedprice * (1 - l_discount)) as promo_revenue\n" +
                        "from\n" +
                        "LINEITEM,\n" +
                        "PART\n" +
                        "where\n" +
                        "l_partkey = p_partkey\n" +
                        "and l_shipdate >= date '1995-09-01'\n" +
                        "and l_shipdate < date '1995-10-01'\n"
        ).executeQuery();
        rs.next();
        
        BigDecimal result = rs.getBigDecimal(1);
        System.out.println("MSQ: " + result);
        return result;
    }
}

