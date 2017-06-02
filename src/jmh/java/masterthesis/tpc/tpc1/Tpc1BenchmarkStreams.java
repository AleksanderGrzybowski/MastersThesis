package masterthesis.tpc.tpc1;

import mastersthesis.BigDecimalSummaryStatistics;
import mastersthesis.Store;
import mastersthesis.Tpc1ResultRow;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Streams are just 2-3x times faster than SQL.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc1BenchmarkStreams {
    
    Store store;
    
    @Param({"0.010", "0.025", "0.050", "0.075", "0.100", "0.125", "0.150", "0.175", "0.200"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public List<Tpc1ResultRow> streams() throws Exception {
        return work(store.getLineItems().stream());
    }
    
    @Benchmark
    public List<Tpc1ResultRow> parallelStreams() throws Exception {
        return work(store.getLineItems().parallelStream());
    }
    
    private List<Tpc1ResultRow> work(Stream<LineItem> stream) throws Exception {
        Map<Pair<String, String>, List<Object>> map = stream.filter(l -> l.shipDate.compareTo(LocalDate.of(1998, 9, 2)) <= 0)
                .collect(
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
        
        results.forEach(System.out::println);
        return results;
    }
}
