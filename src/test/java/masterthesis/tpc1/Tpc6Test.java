package masterthesis.tpc1;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tpc6Test {
    
    @Test
    public void test() throws Exception {
        Tpc6Benchmark benchmark = new Tpc6Benchmark();
        benchmark.setup();
    
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
