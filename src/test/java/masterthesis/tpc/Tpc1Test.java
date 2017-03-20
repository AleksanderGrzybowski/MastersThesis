package masterthesis.tpc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tpc1Test {
    
    @Test
    public void test() throws Exception {
        Tpc1Benchmark benchmark = new Tpc1Benchmark();
        benchmark.scaleFactor = "0.01";
        benchmark.setup();
    
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
