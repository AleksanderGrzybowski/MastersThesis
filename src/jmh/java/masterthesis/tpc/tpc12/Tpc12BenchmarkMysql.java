package masterthesis.tpc.tpc12;

import mastersthesis.Tpc12ResultRow;
import mastersthesis.Utils;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.createSchemaMysql;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc12BenchmarkMysql {
    
    Connection connection;
    
    @Param({"0.01"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase("mysql");
        createSchemaMysql(connection);
    }
    
    @Benchmark
    public List<Tpc12ResultRow> sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
               "select\n" +
                       "\tl_shipmode,\n" +
                       "\tsum(case\n" +
                       "\t\twhen o_orderpriority = '1-URGENT'\n" +
                       "\t\t\tor o_orderpriority = '2-HIGH'\n" +
                       "\t\t\tthen 1\n" +
                       "\t\telse 0\n" +
                       "\tend) as high_line_count,\n" +
                       "\tsum(case\n" +
                       "\t\twhen o_orderpriority <> '1-URGENT'\n" +
                       "\t\t\tand o_orderpriority <> '2-HIGH'\n" +
                       "\t\t\tthen 1\n" +
                       "\t\telse 0\n" +
                       "\tend) as low_line_count\n" +
                       "from\n" +
                       "\tORDERS,\n" +
                       "\tLINEITEM\n" +
                       "where\n" +
                       "\to_orderkey = l_orderkey\n" +
                       "\tand l_shipmode in ('MAIL', 'SHIP')\n" +
                       "\tand l_commitdate < l_receiptdate\n" +
                       "\tand l_shipdate < l_commitdate\n" +
                       "\tand l_receiptdate >= date '1994-01-01'\n" +
                       "\tand l_receiptdate < date '1995-01-01'\n" +
                       "group by\n" +
                       "\tl_shipmode\n" +
                       "order by\n" +
                       "\tl_shipmode;\n"
        ).executeQuery();
    
        List<Tpc12ResultRow> result = new ArrayList<>();
        
        while (rs.next()) {
            result.add(new Tpc12ResultRow(rs.getString(1), rs.getInt(2), rs.getInt(3)));
        }
        
        System.out.println("Mysql : " + result);
        return result;
    }
}

