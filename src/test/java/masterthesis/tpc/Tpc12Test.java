package masterthesis.tpc;

import mastersthesis.Tpc12ResultRow;
import masterthesis.tpc.tpc12.Tpc12BenchmarkH2;
import masterthesis.tpc.tpc12.Tpc12BenchmarkHashmaps;
import masterthesis.tpc.tpc12.Tpc12BenchmarkMysql;
import masterthesis.tpc.tpc12.Tpc12BenchmarkStreams;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Tpc12Test {
    
    @Test
    public void test() throws Exception {
        Tpc12BenchmarkH2 h2 = new Tpc12BenchmarkH2();
        h2.scaleFactor = "0.01";
        h2.setup();
        
        Tpc12BenchmarkMysql mysql = new Tpc12BenchmarkMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
        
        Tpc12BenchmarkStreams streams = new Tpc12BenchmarkStreams();
        streams.scaleFactor = "0.01";
        streams.setup();
        
        Tpc12BenchmarkHashmaps hashmaps = new Tpc12BenchmarkHashmaps();
        hashmaps.scaleFactor = "0.01";
        hashmaps.setup();
        
        List<Tpc12ResultRow> resultH2 = h2.sql();
        List<Tpc12ResultRow> resultMysql = mysql.sql();
        List<Tpc12ResultRow> resultStreams = streams.parallelStreams();
        List<Tpc12ResultRow> resultHashmaps = hashmaps.parallelStreams();
        
        assertEquals(resultH2, resultStreams);
        assertEquals(resultH2, resultMysql);
        assertEquals(resultH2, resultHashmaps);
    }
}
