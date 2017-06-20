package mastersthesis;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.tuple.ImmutablePair.of;

public class JavaMain {
    
    public static void main(String[] args) throws Exception {
        
        List<IntSummaryStatistics> results = Stream.of(
                of(1, 2), of(2, 3),
                of(3, 2), of(4, 1),
                of(2, 0), of(0, 1)
        ).collect(Collector.of(
                () -> asList(new IntSummaryStatistics(), new IntSummaryStatistics()),
                (acc, val) -> {
                    acc.get(0).accept(val.left);
                    acc.get(1).accept(val.right);
                },
                (acc1, acc2) -> {
                    acc1.get(0).combine(acc2.get(0));
                    acc1.get(1).combine(acc2.get(1));
                    return acc1;
                }
        ));
        
        out.println(results.get(0).getSum());
        out.println(results.get(1).getSum());
        
        
    }
}
