package masterthesis.samples;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilteringAndCountingIntegersTest {
    
    @Test
    public void test() throws Exception {
        FilteringAndCountingIntegers benchmark = new FilteringAndCountingIntegers();
        benchmark.setup();
        benchmark.numberCount = 10000;
        benchmark.setup();
        
        int sum1 = benchmark.loop_array();
        int sum2 = benchmark.loop_list();
        int sum3 = benchmark.streams();
        int sum4 = benchmark.sql();
        
        assertEquals(sum1, sum2);
        assertEquals(sum2, sum3);
        assertEquals(sum3, sum4);
    }
}
