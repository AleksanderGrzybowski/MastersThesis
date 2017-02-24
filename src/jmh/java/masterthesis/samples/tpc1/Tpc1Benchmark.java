package masterthesis.samples.tpc1;

import mastersthesis.BigDecimalSummaryStatistics;
import mastersthesis.Store;
import mastersthesis.Tpc1ResultRow;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static mastersthesis.Utils.createSchema;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Tpc1Benchmark {
    
    Connection connection;
    Store store;
    
    @Setup
    public void setup() throws Exception {
        connection = Utils.newDatabase();
        createSchema(connection);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public Object sql() throws Exception {
        ResultSet result = connection.prepareStatement(
                "        select\n" +
                        "                l_returnflag,\n" +
                        "                l_linestatus,\n" +
                        "        sum(l_quantity) as sum_qty,\n" +
                        "        sum(l_extendedprice) as sum_base_price,\n" +
                        "        sum(l_extendedprice * (1 - l_discount)) as sum_disc_price,\n" +
                        "        sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,\n" +
                        "        avg(l_quantity) as avg_qty,\n" +
                        "        avg(l_extendedprice) as avg_price,\n" +
                        "        avg(l_discount) as avg_disc,\n" +
                        "        count(*) as count_order\n" +
                        "        from\n" +
                        "                lineitem\n" +
                        "        where\n" +
                        "        1 = 1\n" +
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
        
        return results;
    }
    
    @Benchmark
    public Object streams() throws Exception {
        Map<Pair<String, String>, List<Object>> map = store.getLineItems().stream().collect(
                Collectors.groupingBy((LineItem e) -> Pair.of(e.returnFlag, e.lineStatus), Collector.of(
                        () -> Arrays.asList(
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics(),
                                new IntSummaryStatistics()
                        ),
                        
                        (result, newValue) -> {
                            ((BigDecimalSummaryStatistics) result.get(0)).accept(newValue.quantity);
                            ((BigDecimalSummaryStatistics) result.get(1)).accept(newValue.extendedPrice);
                            ((BigDecimalSummaryStatistics) result.get(2)).accept(
                                    newValue.extendedPrice.multiply((BigDecimal.ONE.subtract(newValue.discount)
                                    )));
                            ((BigDecimalSummaryStatistics) result.get(3)).accept(
                                    newValue.extendedPrice.multiply((BigDecimal.ONE.subtract(newValue.discount)
                                    )).multiply(BigDecimal.ONE.add(newValue.tax))
                            );
                            ((BigDecimalSummaryStatistics) result.get(4)).accept(newValue.quantity);
                            ((BigDecimalSummaryStatistics) result.get(5)).accept(newValue.extendedPrice);
                            ((BigDecimalSummaryStatistics) result.get(6)).accept(newValue.discount);
                            ((IntSummaryStatistics) result.get(7)).accept(1);
                        },
                        
                        (r1, r2) -> {
                            for (int i = 0; i < 7; i++) {
                                ((BigDecimalSummaryStatistics) r1.get(i)).combine(
                                        ((BigDecimalSummaryStatistics) r2.get(i))
                                );
                            }
                            ((IntSummaryStatistics) r1.get(7)).combine(
                                    ((IntSummaryStatistics) r2.get(7))
                            );
                            return r1;
                        }
                ))
        );
        
        List<Tpc1ResultRow> results = new ArrayList<>();
        TreeMap<Pair<String, String>, List<Object>> sorted = new TreeMap<>(
                Comparator.comparing((Function<Pair<String, String>, String>) Pair::getLeft)
                        .thenComparing(Pair::getRight)
        );
        sorted.putAll(map);
        
        for (Map.Entry<Pair<String, String>, List<Object>> entry : sorted.entrySet()) {
            Tpc1ResultRow row = new Tpc1ResultRow();
            row.l_returnflag = entry.getKey().getLeft();
            row.l_linestatus = entry.getKey().getRight();
            row.sum_qty = ((BigDecimalSummaryStatistics) entry.getValue().get(0)).getSum();
            row.sum_base_price = ((BigDecimalSummaryStatistics) entry.getValue().get(1)).getSum();
            row.sum_disc_price = ((BigDecimalSummaryStatistics) entry.getValue().get(2)).getSum();
            row.sum_charge = ((BigDecimalSummaryStatistics) entry.getValue().get(3)).getSum();
            row.avg_qty = ((BigDecimalSummaryStatistics) entry.getValue().get(4)).getAverage();
            row.avg_price = ((BigDecimalSummaryStatistics) entry.getValue().get(5)).getAverage();
            row.avg_disc = ((BigDecimalSummaryStatistics) entry.getValue().get(6)).getAverage();
            row.count_order = ((IntSummaryStatistics) entry.getValue().get(7)).getSum();
            results.add(row);
        }
        
        return results;
    }
}
