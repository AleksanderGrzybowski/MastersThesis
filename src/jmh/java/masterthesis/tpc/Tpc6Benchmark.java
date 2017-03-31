

package masterthesis.tpc;

import mastersthesis.Store;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.stream.Stream;

import static mastersthesis.Utils.createSchema;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc6Benchmark {
    
    Connection connection;
    Store store;
    
    @Param({"0.01", "0.02", "0.03", "0.05"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase();
        createSchema(connection);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public BigDecimal sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
                "select\n" +
                        "sum(l_extendedprice * l_discount) as revenue\n" +
                        "from\n" +
                        "lineitem\n" +
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
    
    @Benchmark
    public BigDecimal streams() throws Exception {
        return work(store.getLineItems().stream());
    }
    
    @Benchmark
    public BigDecimal parallelStreams() throws Exception {
        return work(store.getLineItems().parallelStream());
    }
    
    private BigDecimal work(Stream<LineItem> stream) {
        BigDecimal result = stream.filter(item -> (
                item.shipDate.compareTo(LocalDate.of(1994, 1, 1)) >= 0 &&
                        item.shipDate.compareTo(LocalDate.of(1995, 1, 1)) < 0 &&
                        item.discount.compareTo(new BigDecimal("0.05")) >= 0 &&
                        item.discount.compareTo(new BigDecimal("0.07")) <= 0 &&
                        item.quantity.intValue() < 24
        ))
                .map(item -> item.extendedPrice.multiply(item.discount))
                .reduce(BigDecimal::add).orElseThrow(RuntimeException::new);
        
        System.out.println(result);
        return result;
    }
}

