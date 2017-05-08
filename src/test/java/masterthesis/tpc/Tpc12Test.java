package masterthesis.tpc;

import mastersthesis.Tpc12ResultRow;
import mastersthesis.Utils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Tpc12Test {
    
    @Test
    public void test() throws Exception {
        Utils.recreateData("0.01");
        
        Tpc12BenchmarkH2 h2 = new Tpc12BenchmarkH2();
        h2.setup();
        
        Tpc12BenchmarkMysql mysql = new Tpc12BenchmarkMysql();
        mysql.setup();
        
        Tpc12BenchmarkStreams streams = new Tpc12BenchmarkStreams();
        streams.setup();
        
        List<Tpc12ResultRow> resultH2 = h2.sql();
        List<Tpc12ResultRow> resultMysql = mysql.sql();
        List<Tpc12ResultRow> resultStreams = streams.parallelStreams();
        
        assertEquals(resultH2, resultStreams);
        assertEquals(resultH2, resultMysql);
    }
}
