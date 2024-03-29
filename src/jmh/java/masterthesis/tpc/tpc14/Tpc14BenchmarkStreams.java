package masterthesis.tpc.tpc14;

import mastersthesis.BigDecimalSummaryStatistics;
import mastersthesis.Store;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import mastersthesis.model.Part;
import masterthesis.tpc.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Because of joins, SQL rocks here and streams are 50x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc14BenchmarkStreams {
    
    Store store;
    
    @Param({"0.01"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public BigDecimal streams() throws Exception {
        return work(
                StreamUtils.innerJoin(
                        store.getParts(),
                        store.getLineItems(),
                        pair -> pair.getLeft().partkey == pair.getRight().getPartKey()
                ));
    }
    
    @Benchmark
    public BigDecimal parallelStreams() throws Exception {
        return work(
                StreamUtils.innerJoin(
                        store.getParts(),
                        store.getLineItems(),
                        pair -> pair.getLeft().partkey == pair.getRight().getPartKey()
                ).parallel());
    }
    
    private BigDecimal work(Stream<ImmutablePair<Part, LineItem>> stream) throws Exception {
        List<BigDecimalSummaryStatistics> list = stream.filter(pair -> (
                pair.getRight().shipDate.compareTo(LocalDate.of(1995, 9, 1)) >= 0 &&
                        pair.getRight().shipDate.compareTo(LocalDate.of(1995, 10, 1)) < 0
        )).collect(
                Collector.of(
                        () -> Arrays.asList(
                                new BigDecimalSummaryStatistics(),
                                new BigDecimalSummaryStatistics()
                        ),
                        (List<BigDecimalSummaryStatistics> accumulator, Pair<Part, LineItem> newValue) -> {
                            accumulator.get(0).accept(
                                    newValue.getLeft().type.startsWith("PROMO") ? (
                                            newValue.getRight().extendedPrice.multiply(BigDecimal.ONE.subtract
                                                    (newValue.getRight().discount))
                                    ) : BigDecimal.ZERO);
                            accumulator.get(1).accept(
                                    newValue.getRight().extendedPrice.multiply(BigDecimal.ONE.subtract
                                            (newValue.getRight().discount))
                            );
                        },
                        (r1, r2) -> {
                            for (int i = 0; i < 2; i++) {
                                r1.get(i).combine(r2.get(i));
                            }
                            return r1;
                        }
                )
        );
        BigDecimal result = new BigDecimal("100.00")
                .multiply(list.get(0).getSum())
                .divide(list.get(1).getSum(), RoundingMode.HALF_UP);
        
        System.out.println("J8S: " + result);
        return result;
    }
}

