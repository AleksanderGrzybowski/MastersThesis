package masterthesis.samples;

import masterthesis.samples.naturaljoin.*;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class NaturalJoinTest {
    
    @Test
    public void test() throws Exception {
        NaturalJoinStreamsNaive streamsNaive = new NaturalJoinStreamsNaive();
        streamsNaive.scaleFactor = "0.01";
        streamsNaive.setup();
        
        NaturalJoinStreamsForLoop streamsForLoop = new NaturalJoinStreamsForLoop();
        streamsForLoop.scaleFactor = "0.01";
        streamsForLoop.setup();
        
        NaturalJoinStreamsHashmaps streamsHashmaps = new NaturalJoinStreamsHashmaps();
        streamsHashmaps.scaleFactor = "0.01";
        streamsHashmaps.setup();
        
        NaturalJoinH2 h2 = new NaturalJoinH2();
        h2.scaleFactor = "0.01";
        h2.setup();
        
        NaturalJoinMysql mysql = new NaturalJoinMysql();
        mysql.scaleFactor = "0.01";
        mysql.setup();
        
        Object resultStreamsNative = new HashSet<>(streamsNaive.sql());
        Object resultStreamsForLoop = new HashSet<>(streamsForLoop.sql());
        Object resultStreamsHashmaps = new HashSet<>(streamsHashmaps.sql());
        Object resultsH2 = new HashSet<>(h2.sql());
        Object resultsMysql = new HashSet<>(mysql.sql());
        
        assertEquals(resultStreamsNative, resultStreamsForLoop);
        assertEquals(resultStreamsNative, resultStreamsHashmaps);
        assertEquals(resultStreamsNative, resultsH2);
        assertEquals(resultStreamsNative, resultsMysql);
    }
}
