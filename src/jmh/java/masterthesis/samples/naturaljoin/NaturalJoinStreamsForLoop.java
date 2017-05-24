package masterthesis.samples.naturaljoin;

import mastersthesis.Store;
import mastersthesis.Utils;
import masterthesis.tpc.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class NaturalJoinStreamsForLoop {
    
    Store store;
    
    @Param({"0.01"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        store = new Store("dbgen");
    }
    
//    @Benchmark // also very slow
    public List<ImmutablePair<Long, Long>> sql() throws Exception {
        
        List<ImmutablePair<Long, Long>> result = StreamUtils.innerJoinForLoop(
                store.getOrders(),
                store.getLineItems(),
                (left, right) -> left.equals(right.order)
        ).map(pair -> new ImmutablePair<>(pair.left.orderkey, pair.right.order.orderkey))
                .collect(Collectors.toList());
        
        
        System.out.println("ForLoop streams count(*) : " + result.size());
        return result;
    }
}

