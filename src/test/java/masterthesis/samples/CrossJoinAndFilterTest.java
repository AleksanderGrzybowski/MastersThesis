package masterthesis.samples;

import masterthesis.samples.crossjoinandfilter.CrossJoinAndFilterH2;
import masterthesis.samples.crossjoinandfilter.CrossJoinAndFilterMysql;
import masterthesis.samples.crossjoinandfilter.CrossJoinAndFilterStreams;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CrossJoinAndFilterTest {
    
    @Test
    public void test() throws Exception {
        CrossJoinAndFilterStreams streams = new CrossJoinAndFilterStreams();
        streams.numberCount = 100;
        streams.setup();
    
        CrossJoinAndFilterMysql mysql = new CrossJoinAndFilterMysql();
        mysql.numberCount = 100;
        mysql.setup();
        
        CrossJoinAndFilterH2 h2 = new CrossJoinAndFilterH2();
        h2.numberCount = 100;
        h2.setup();
    
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultMysql = mysql.sql();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultH2 = h2.sql();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultStreams = streams.streams();
        List<Pair<Pair<Integer, Integer>, Pair<Integer, String>>> resultParallelStreams = streams.parallelStreams();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
