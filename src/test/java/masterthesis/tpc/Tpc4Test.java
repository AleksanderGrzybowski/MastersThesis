package masterthesis.tpc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tpc4Test {
    
    @Test
    public void test() throws Exception {
        Tpc4Benchmark benchmark = new Tpc4Benchmark();
        benchmark.scaleFactor = "0.01";
        benchmark.setup();
        
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
