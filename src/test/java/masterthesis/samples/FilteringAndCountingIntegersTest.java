package masterthesis.samples;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilteringAndCountingIntegersTest {
    
    @Test
    public void test() throws Exception {
        FilteringAndCountingIntegersStreams streams = new FilteringAndCountingIntegersStreams();
        streams.numberCount = 100;
        streams.setup();
        
        FilteringAndCountingIntegersMysql mysql = new FilteringAndCountingIntegersMysql();
        mysql.numberCount = 100;
        mysql.setup();
        
        FilteringAndCountingIntegersH2 h2 = new FilteringAndCountingIntegersH2();
        h2.numberCount = 100;
        h2.setup();
        
        int resultMysql = mysql.sql();
        int resultH2 = h2.sql();
        int resultStreams = streams.streams();
        int resultParallelStreams = streams.parallelStreams();
        
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
