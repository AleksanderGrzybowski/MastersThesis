package masterthesis.samples;

import masterthesis.samples.sortingstrings.SortingStringsH2;
import masterthesis.samples.sortingstrings.SortingStringsMysql;
import masterthesis.samples.sortingstrings.SortingStringsStreams;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SortingStringsTest {
    
    @Test
    public void test() throws Exception {
        SortingStringsStreams streams = new SortingStringsStreams();
        streams.count = 100;
        streams.setup();
    
        SortingStringsMysql mysql = new SortingStringsMysql();
        mysql.count = 100;
        mysql.setup();
    
        SortingStringsH2 h2 = new SortingStringsH2();
        h2.count = 100;
        h2.setup();
    
        List<String> resultMysql = mysql.sql();
        List<String> resultH2 = h2.sql();
        List<String> resultStreams = streams.stream();
        List<String> resultParallelStreams = streams.parallelStream();
    
        assertEquals(resultMysql, resultH2);
        assertEquals(resultH2, resultStreams);
        assertEquals(resultStreams, resultParallelStreams);
    }
}
