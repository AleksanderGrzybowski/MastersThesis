package masterthesis.tpc;

import mastersthesis.Tpc4ResultRow;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Tpc4Test {
    
    @Test
    public void test() throws Exception {
        Tpc4BenchmarkStreams streams = new Tpc4BenchmarkStreams();
        streams.scaleFactor = "0.01";
        streams.setup();
    
        Tpc4BenchmarkH2 h2 = new Tpc4BenchmarkH2();
        h2.scaleFactor = "0.01";
        h2.setup();
    
        Tpc4BenchmarkMysql mysql = new Tpc4BenchmarkMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
    
        List<Tpc4ResultRow> resultStreams = streams.streams();
        List<Tpc4ResultRow> resultH2 = h2.sql();
        List<Tpc4ResultRow> resultMysql = mysql.sql();
    
        assertEquals(resultStreams, resultMysql);
        assertEquals(resultMysql, resultH2);
    }
}
