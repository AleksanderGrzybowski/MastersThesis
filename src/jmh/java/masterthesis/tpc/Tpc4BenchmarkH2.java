package masterthesis.tpc;

import mastersthesis.Tpc4ResultRow;
import mastersthesis.Utils;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.createSchemaH2;

/**
 * Because of inner query, SQL is blazing fast, and streams are 100x slower than this.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc4BenchmarkH2 {
    
    Connection connection;
    
    @Param({"0.01", "0.02"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase("h2");
        createSchemaH2(connection);
    }
    
    @Benchmark
    public List<Tpc4ResultRow> sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
                "select\n" +
                        "o_orderpriority,\n" +
                        "count(*) as order_count\n" +
                        "from\n" +
                        "orders\n" +
                        "where\n" +
                        "o_orderdate >= date '1993-07-01'\n" +
                        "and o_orderdate < date '1993-10-01'\n" +
                        "and exists (\n" +
                        "select\n" +
                        "*\n" +
                        "from\n" +
                        "lineitem\n" +
                        "where\n" +
                        "l_orderkey = o_orderkey\n" +
                        "and l_commitdate < l_receiptdate\n" +
                        ")\n" +
                        "group by\n" +
                        "o_orderpriority\n" +
                        "order by\n" +
                        "o_orderpriority;\n"
        
        ).executeQuery();
        
        List<Tpc4ResultRow> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(new Tpc4ResultRow(rs.getString(1), rs.getLong(2)));
        }
        
        rows.forEach(System.out::println);
        return rows;
    }
}

