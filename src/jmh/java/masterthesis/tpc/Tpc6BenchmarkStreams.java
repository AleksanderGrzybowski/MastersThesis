package masterthesis.tpc;

import mastersthesis.Store;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * SQL is 2-3x slower.
 */
@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class Tpc6BenchmarkStreams {
    
    Store store;
    
    @Param({"0.01", "0.02", "0.05", "0.1", "0.2"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        store = new Store("dbgen");
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

