package masterthesis.tpc;

import masterthesis.tpc.tpc6.Tpc6BenchmarkH2;
import masterthesis.tpc.tpc6.Tpc6BenchmarkMysql;
import masterthesis.tpc.tpc6.Tpc6BenchmarkStreams;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class Tpc6Test {
    
    @Test
    public void test() throws Exception {
        Tpc6BenchmarkStreams streams = new Tpc6BenchmarkStreams();
        streams.scaleFactor = "0.01";
        streams.setup();
    
        Tpc6BenchmarkH2 h2 = new Tpc6BenchmarkH2();
        h2.scaleFactor = "0.01";
        h2.setup();
    
        Tpc6BenchmarkMysql mysql = new Tpc6BenchmarkMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
    
        BigDecimal resultStreams = streams.streams();
        BigDecimal resultH2 = h2.sql();
        BigDecimal resultMysql = mysql.sql();
    
        assertEquals(resultStreams, resultMysql);
        assertEquals(resultMysql, resultH2);
    }
}
