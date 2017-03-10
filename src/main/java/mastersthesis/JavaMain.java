package mastersthesis;

import masterthesis.samples.tpc1.Tpc1Benchmark;

import java.util.List;

public class JavaMain {
    
    public static void main(String[] args) throws Exception {
        Tpc1Benchmark benchmark = new Tpc1Benchmark();
        benchmark.setup();
        List<Tpc1ResultRow> sqlResults = benchmark.sql();
        List<Tpc1ResultRow> streamResults = benchmark.streams();
    
        System.out.println(sqlResults.equals(streamResults));
    }
}
