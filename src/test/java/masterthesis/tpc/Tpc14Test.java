package masterthesis.tpc;

import org.junit.Test;

import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class Tpc14Test {
    
    @Test
    public void test() throws Exception {
        Tpc14Benchmark benchmark = new Tpc14Benchmark();
        benchmark.setup();
        
        assertEquals(
                benchmark.sql().setScale(5, RoundingMode.HALF_UP),
                benchmark.streams().setScale(5, RoundingMode.HALF_UP)
        );
    }
}
