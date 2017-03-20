package masterthesis.tpc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tpc6Test {
    
    @Test
    public void test() throws Exception {
        Tpc6Benchmark benchmark = new Tpc6Benchmark();
        benchmark.scaleFactor = "0.01";
        benchmark.setup();
    
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
