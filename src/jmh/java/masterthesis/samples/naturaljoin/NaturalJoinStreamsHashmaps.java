package masterthesis.samples.naturaljoin;

import mastersthesis.Store;
import mastersthesis.Utils;
import masterthesis.tpc.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class NaturalJoinStreamsHashmaps {
    
    Store store;
    
    @Param({"0.010", "0.025", "0.050", "0.075", "0.100", "0.125", "0.150", "0.175", "0.200"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        store = new Store("dbgen");
    }
    
    @Benchmark
    public List<ImmutablePair<Long, Long>> hashMaps() throws Exception {
        
        List<ImmutablePair<Long, Long>> result = StreamUtils.innerJoinHashmaps(
                store.getOrders(),
                o -> o.orderkey,
                store.getLineItems(),
                l -> l.order.orderkey
        )
                .map(pair -> new ImmutablePair<>(pair.left.orderkey, pair.right.order.orderkey))
                .collect(Collectors.toList());
        
        System.out.println("Hashmaps streams count(*) : " + result.size());
        return result;
    }
}

