

package masterthesis.tpc;

import mastersthesis.Store;
import mastersthesis.Tpc4ResultRow;
import mastersthesis.Utils;
import mastersthesis.model.Order;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static mastersthesis.Utils.createSchema;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc4Benchmark {
    
    Connection connection;
    Store store;
    
    @Param({"0.01", "0.02"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase();
        createSchema(connection);
        store = new Store("dbgen");
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
    
    @Benchmark
    public List<Tpc4ResultRow> streams() throws Exception {
        return work(store.getOrders().stream());
    }
    
    @Benchmark
    public List<Tpc4ResultRow> parallelStreams() throws Exception {
        return work(store.getOrders().parallelStream());
    }
    
    private List<Tpc4ResultRow> work(Stream<Order> stream) {
        List<Tpc4ResultRow> rows = stream
                .filter(order -> (
                        order.orderDate.compareTo(LocalDate.of(1993, 7, 1)) >= 0
                                &&
                                order.orderDate.compareTo(LocalDate.of(1993, 10, 1)) < 0
                                &&
                                store.getLineItems().stream().filter(lineItem ->
                                        lineItem.order.orderkey == order.orderkey
                                                &&
                                                lineItem.commitDate.compareTo(lineItem.receiptDate) < 0
                                ).count() != 0
                ))
                .collect(Collectors.groupingBy(order -> order.orderPriority, Collectors.counting()))
                
                .entrySet().stream().map(entry -> new Tpc4ResultRow(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(e -> e.orderPriority))
                .collect(toList());
        
        rows.forEach(System.out::println);
        return rows;
    }
}

