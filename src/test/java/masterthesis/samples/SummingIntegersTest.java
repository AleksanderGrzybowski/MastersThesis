package masterthesis.samples;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SummingIntegersTest {
    
    @Test
    public void test() throws Exception {
        SummingIntegers benchmark = new SummingIntegers();
        benchmark.numberCount = 10000;
        benchmark.setup();
        
        int sum1 = benchmark.loop_over_list();
        int sum2 = benchmark.loop_over_array();
        int sum3 = benchmark.stream();
        int sum4 = benchmark.parallelStream();
        int sum5 = benchmark.sql();
        
        assertEquals(sum1, sum2);
        assertEquals(sum2, sum3);
        assertEquals(sum3, sum4);
        assertEquals(sum4, sum5);
    }
}
