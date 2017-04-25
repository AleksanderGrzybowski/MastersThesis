package masterthesis.tpc;

import mastersthesis.Tpc1ResultRow;
import mastersthesis.Utils;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.createSchemaH2;

/**
 * Streams are just 2-3x times faster than SQL.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc1BenchmarkH2 {
    
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
    public List<Tpc1ResultRow> sql() throws Exception {
        ResultSet result = connection.prepareStatement(
                "        select\n" +
                        "                l_returnflag,\n" +
                        "                l_linestatus,\n" +
                        "        sum(l_quantity) as sum_qty,\n" +
                        "                        sum(l_extendedprice) as sum_base_price,\n" +
                        "        sum(l_extendedprice * (1 - l_discount)) as sum_disc_price,\n" +
                        "        sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,\n" +
                        "        avg(l_quantity) as avg_qty,\n" +
                        "        avg(l_extendedprice) as avg_price,\n" +
                        "        avg(l_discount) as avg_disc,\n" +
                        "        count(*) as count_order\n" +
                        "        from\n" +
                        "                lineitem\n" +
                        "        where l_shipdate <=  DATE '1998-09-02'" + // hard-coded from documentation
                        "        group by\n" +
                        "        l_returnflag,\n" +
                        "                l_linestatus\n" +
                        "        order by\n" +
                        "        l_returnflag,\n" +
                        "                l_linestatus;\n"
        ).executeQuery();
        
        List<Tpc1ResultRow> results = new ArrayList<>();
        while (result.next()) {
            Tpc1ResultRow row = new Tpc1ResultRow();
            row.l_returnflag = result.getString(1);
            row.l_linestatus = result.getString(2);
            row.sum_qty = result.getBigDecimal(3);
            row.sum_base_price = result.getBigDecimal(4);
            row.sum_disc_price = result.getBigDecimal(5);
            row.sum_charge = result.getBigDecimal(6);
            row.avg_qty = result.getBigDecimal(7);
            row.avg_price = result.getBigDecimal(8);
            row.avg_disc = result.getBigDecimal(9);
            row.count_order = result.getLong(10);
            results.add(row);
        }
        
        results.forEach(System.out::println);
        return results;
    }
}
