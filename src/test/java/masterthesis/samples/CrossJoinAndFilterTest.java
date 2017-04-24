package masterthesis.samples;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CrossJoinAndFilterTest {
    
    @Test
    public void test() throws Exception {
        CrossJoinAndFilterStreams streams = new CrossJoinAndFilterStreams();
        streams.setup();
        streams.numberCount = 100;
    
        CrossJoinAndFilterMysql mysql = new CrossJoinAndFilterMysql();
        mysql.setup();
        mysql.numberCount = 100;
        
        CrossJoinAndFilterH2 h2 = new CrossJoinAndFilterH2();
        h2.setup();
        h2.numberCount = 100;
    
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultMysql = mysql.sql();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultH2 = h2.sql();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultStreams = streams.streams();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultParallelStreams = streams.parallelStreams();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
