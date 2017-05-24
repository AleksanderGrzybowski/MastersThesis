package masterthesis.samples;

import masterthesis.samples.hugejoining.HugeJoiningH2;
import masterthesis.samples.hugejoining.HugeJoiningMysql;
import masterthesis.samples.hugejoining.HugeJoiningStreams;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HugeJoiningTest {
    
    @Test
    public void test() throws Exception {
        HugeJoiningStreams streams = new HugeJoiningStreams();
        streams.numberCount = 10;
        streams.setup();
    
        HugeJoiningMysql mysql = new HugeJoiningMysql();
        mysql.numberCount = 10;
        mysql.setup();
    
        HugeJoiningH2 h2 = new HugeJoiningH2();
        h2.numberCount = 10;
        h2.setup();
    
        List<Pair<Integer, Integer>> resultMysql = mysql.sql();
        List<Pair<Integer, Integer>> resultH2 = h2.sql();
        List<Pair<Integer, Integer>> resultStreams = streams.streams();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
    }
}
