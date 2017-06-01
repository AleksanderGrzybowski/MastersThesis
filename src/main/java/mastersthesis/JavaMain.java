package mastersthesis;

import java.time.LocalDate;
import java.time.Month;

public class JavaMain {
    
    public static void main(String[] args) throws Exception {
        System.out.println(
                LocalDate.of(2017, Month.JANUARY, 31).plusMonths(1)
        );
    }
}
