package masterthesis.samples;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CrossJoinAndFilterTest {
    
    @Test
    public void test() throws Exception {
        CrossJoinAndFilter benchmark = new CrossJoinAndFilter();
        benchmark.setup();
        benchmark.numberCount = 100;
        benchmark.setup();
    
        assertEquals(benchmark.sql(), benchmark.streams());
    }
}
