
package masterthesis.tpc;

import mastersthesis.Store;
import mastersthesis.Tpc12ResultRow;
import mastersthesis.model.LineItem;
import mastersthesis.model.Order;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc12BenchmarkHashmaps {
    
    Store store;
    
    @Param({"0.01"})
    public String scaleFactor;
    
    public static Stream<ImmutablePair<Order, LineItem>> innerJoinHashmaps(
            Collection<Order> first,
            Collection<LineItem> second
    ) {
        HashMap<Long, Order> ordersMap = new HashMap<>();
        for (Order order : first) {
            ordersMap.put(order.orderkey, order);
        }
        
        List<ImmutablePair<Order, LineItem>> result = new ArrayList<>();
        
        for (LineItem lineItem : second) {
            Order order = ordersMap.get(lineItem.order.orderkey);
            if (order != null) {
                result.add(new ImmutablePair<>(order, lineItem));
            }
        }
        
        return result.stream();
    }
    
    @Setup
    public void setup() throws Exception {
        store = new Store("dbgen");
    }
    
    @Benchmark
    public List<Tpc12ResultRow> parallelStreams() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        Map<String, List<IntSummaryStatistics>> collect = innerJoinHashmaps(store.getOrders(), store.getLineItems()).parallel()
                .filter(pair -> (Objects.equals(pair.right.shipMode, "MAIL") || Objects.equals(pair.right.shipMode, "SHIP"))
                        &&
                        pair.right.commitDate.compareTo(pair.right.receiptDate) < 0
                        &&
                        pair.right.shipDate.compareTo(pair.right.commitDate) < 0
                        &&
                        pair.right.receiptDate.compareTo(LocalDate.of(1994, 1, 1)) >= 0
                        &&
                        pair.right.receiptDate.compareTo(LocalDate.of(1995, 1, 1)) < 0
                ).collect(
                        Collectors.groupingBy((Pair<Order, LineItem> pair) -> pair.getRight().shipMode, Collector.of(
                                () -> asList(
                                        new IntSummaryStatistics(),
                                        new IntSummaryStatistics()
                                ),
                                (List<IntSummaryStatistics> result, Pair<Order, LineItem> newValue) -> {
                                    String orderPriority = newValue.getLeft().orderPriority;
                                    result.get(0).accept(
                                            ((Objects.equals(orderPriority, "1-URGENT")) || Objects.equals(orderPriority, "2-HIGH")) ? 1 : 0
                                    );
                                    result.get(1).accept(
                                            ((!Objects.equals(orderPriority, "1-URGENT")) && !Objects.equals(orderPriority, "2-HIGH")) ? 1 : 0
                                    );
                                },
                                
                                (List<IntSummaryStatistics> r1, List<IntSummaryStatistics> r2) -> {
                                    r1.get(0).combine(r2.get(0));
                                    r1.get(1).combine(r2.get(1));
                                    return r1;
                                }
                        ))
                );
        
        System.out.println(1);
        List<Tpc12ResultRow> xx = collect.entrySet().stream()
                .map(entry -> new Tpc12ResultRow(
                        entry.getKey(),
                        (int) entry.getValue().get(0).getSum(),
                        (int) entry.getValue().get(1).getSum())
                ).collect(toList());
        
        
        System.out.println("Hashmaps: " + xx);
        
        return xx;
    }
}

