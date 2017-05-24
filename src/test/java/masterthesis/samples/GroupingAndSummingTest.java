package masterthesis.samples;

import masterthesis.samples.groupingandsumming.GroupingAndSummingH2;
import masterthesis.samples.groupingandsumming.GroupingAndSummingMysql;
import masterthesis.samples.groupingandsumming.GroupingAndSummingStreams;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GroupingAndSummingTest {
    
    @Test
    public void test() throws Exception {
        GroupingAndSummingStreams streams = new GroupingAndSummingStreams();
        streams.numberCount = 100;
        streams.setup();
    
        GroupingAndSummingMysql mysql = new GroupingAndSummingMysql();
        mysql.numberCount = 100;
        mysql.setup();
    
        GroupingAndSummingH2 h2 = new GroupingAndSummingH2();
        h2.numberCount = 100;
        h2.setup();
    
        Map<Integer, Integer> resultMysql = mysql.sql();
        Map<Integer, Integer> resultH2 = h2.sql();
        Map<Integer, Integer> resultStreams = streams.streams();
        Map<Integer, Integer> resultParallelStreams = streams.parallelStreams();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
