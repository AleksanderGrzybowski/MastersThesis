package masterthesis.tpc1;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tpc1Test {
    
    @Test
    public void test() throws Exception {
        Tpc1Benchmark benchmark = new Tpc1Benchmark();
        benchmark.setup();
    
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
