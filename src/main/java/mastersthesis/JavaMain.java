package mastersthesis;

import java.util.stream.IntStream;

public class JavaMain {
    
    public static void main(String[] args) throws Exception {
        IntStream.range(1, 100).parallel()
                .forEach(e -> System.out.println(
                        Thread.currentThread().getName()
                        )
                );

    }
}
