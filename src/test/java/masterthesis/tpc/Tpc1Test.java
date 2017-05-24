package masterthesis.tpc;

import mastersthesis.Tpc1ResultRow;
import masterthesis.tpc.tpc1.Tpc1BenchmarkH2;
import masterthesis.tpc.tpc1.Tpc1BenchmarkMysql;
import masterthesis.tpc.tpc1.Tpc1BenchmarkStreams;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Tpc1Test {
    
    @Test
    public void test() throws Exception {
        Tpc1BenchmarkStreams streams = new Tpc1BenchmarkStreams();
        streams.scaleFactor = "0.01";
        streams.setup();
        
        Tpc1BenchmarkH2 h2 = new Tpc1BenchmarkH2();
        h2.scaleFactor = "0.01";
        h2.setup();
        
        Tpc1BenchmarkMysql mysql = new Tpc1BenchmarkMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
    
        List<Tpc1ResultRow> resultStreams = streams.streams();
        List<Tpc1ResultRow> resultH2 = h2.sql();
        List<Tpc1ResultRow> resultMysql = mysql.sql();
    
        assertEquals(resultStreams, resultMysql);
        assertEquals(resultMysql, resultH2);
    }
}
