package masterthesis.samples;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GroupingAndSummingTest {
    
    @Test
    public void test() throws Exception {
        GroupingAndSumming benchmark = new GroupingAndSumming();
        benchmark.setup();
        benchmark.numberCount = 10000;
        benchmark.setup();
        
        Map<Integer, Integer> sum1 = benchmark.loop();
        Map<Integer, Integer> sum2 = benchmark.sql();
        Map<Integer, Integer> sum3 = benchmark.streams();
        
        assertEquals(sum1, sum2);
        assertEquals(sum2, sum3);
    }
}
