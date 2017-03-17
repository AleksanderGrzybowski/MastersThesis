package masterthesis.tpc;

import mastersthesis.BigDecimalSummaryStatistics;
import mastersthesis.Store;
import mastersthesis.Utils;
import mastersthesis.model.LineItem;
import mastersthesis.model.Part;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

import static mastersthesis.Utils.createSchema;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Tpc14Benchmark {
    
    Connection connection;
    Store store;
    
    @Setup
    public void setup() throws Exception {
        connection = Utils.newDatabase();
        createSchema(connection);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public BigDecimal sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
                "select\n" +
                        "100.00 * sum(case\n" +
                        "when p_type like 'PROMO%'\n" +
                        "then l_extendedprice * (1 - l_discount)\n" +
                        "else 0\n" +
                        "end) / sum(l_extendedprice * (1 - l_discount)) as promo_revenue\n" +
                        "from\n" +
                        "lineitem,\n" +
                        "part\n" +
                        "where\n" +
                        "l_partkey = p_partkey\n" +
                        "and l_shipdate >= date '1995-09-01'\n" +
                        "and l_shipdate < date '1995-10-01'\n"
        ).executeQuery();
        rs.next();
        
        BigDecimal result = rs.getBigDecimal(1);
        System.out.println("SQL: " + result);
        return result;
    }
    
    @Benchmark
    public BigDecimal streams() throws Exception {
        List<BigDecimalSummaryStatistics> list = StreamUtils.innerJoin(
                store.getParts(),
                store.getLineItems(),
                pair -> pair.getLeft().partkey == pair.getRight().getPartKey()
        )
                .filter(pair -> (
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

