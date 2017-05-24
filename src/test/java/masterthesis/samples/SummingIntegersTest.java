package masterthesis.samples;

import masterthesis.samples.summingintegers.SummingIntegersH2;
import masterthesis.samples.summingintegers.SummingIntegersMysql;
import masterthesis.samples.summingintegers.SummingIntegersStreams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SummingIntegersTest {
    
    @Test
    public void test() throws Exception {
        SummingIntegersStreams streams = new SummingIntegersStreams();
        streams.numberCount = 100;
        streams.setup();
    
        SummingIntegersMysql mysql = new SummingIntegersMysql();
        mysql.numberCount = 100;
        mysql.setup();
    
        SummingIntegersH2 h2 = new SummingIntegersH2();
        h2.numberCount = 100;
        h2.setup();
    
        int resultMysql = mysql.sql();
        int resultH2 = h2.sql();
        int resultStreams = streams.stream();
        int resultParallelStreams = streams.parallelStream();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
