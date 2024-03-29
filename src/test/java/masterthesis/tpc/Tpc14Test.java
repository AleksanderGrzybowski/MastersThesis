package masterthesis.tpc;

import masterthesis.tpc.tpc14.Tpc14BenchmarkH2;
import masterthesis.tpc.tpc14.Tpc14BenchmarkHashmaps;
import masterthesis.tpc.tpc14.Tpc14BenchmarkMysql;
import masterthesis.tpc.tpc14.Tpc14BenchmarkStreams;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class Tpc14Test {
    
    @Test
    public void test() throws Exception {
        Tpc14BenchmarkStreams streams = new Tpc14BenchmarkStreams();
        streams.scaleFactor = "0.01";
        streams.setup();
        
        Tpc14BenchmarkHashmaps hashmaps = new Tpc14BenchmarkHashmaps();
        hashmaps.scaleFactor = "0.01";
        hashmaps.setup();
    
        Tpc14BenchmarkH2 h2 = new Tpc14BenchmarkH2();
        h2.scaleFactor = "0.01";
        h2.setup();
    
        Tpc14BenchmarkMysql mysql = new Tpc14BenchmarkMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
    
        BigDecimal resultStreams = streams.streams();
        BigDecimal resultHashmaps = hashmaps.hashmaps();
        BigDecimal resultH2 = h2.sql();
        BigDecimal resultMysql = mysql.sql();
    
        assertEquals(resultStreams.setScale(5, RoundingMode.HALF_UP), resultMysql.setScale(5, RoundingMode.HALF_UP));
        assertEquals(resultStreams.setScale(5, RoundingMode.HALF_UP), resultHashmaps.setScale(5, RoundingMode.HALF_UP));
        assertEquals(resultMysql.setScale(5, RoundingMode.HALF_UP), resultH2.setScale(5, RoundingMode.HALF_UP));
    }
}
