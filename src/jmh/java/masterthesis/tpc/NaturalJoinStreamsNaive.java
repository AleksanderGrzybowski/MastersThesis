package masterthesis.tpc;

import mastersthesis.Store;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class NaturalJoinStreamsNaive {
    
    Store store;
    
    
    @Param({"0.01"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        store = new Store("dbgen");
    }
    
//    @Benchmark // too slow
    public List<ImmutablePair<Long, Long>> sql() throws Exception {
        
        List<ImmutablePair<Long, Long>> result = StreamUtils.innerJoin(
                store.getOrders(),
                store.getLineItems(),
                pair -> pair.left.equals(pair.right.order)
        ).map(pair -> new ImmutablePair<>(pair.left.orderkey, pair.right.order.orderkey))
                .collect(Collectors.toList());
        
        
        System.out.println("Naive streams count(*) : " + result.size());
        return result;
    }
}
