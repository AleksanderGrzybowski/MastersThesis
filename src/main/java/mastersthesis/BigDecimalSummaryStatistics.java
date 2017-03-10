package mastersthesis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BigDecimalSummaryStatistics {
    
    private List<BigDecimal> list = new ArrayList<>();
    
    public void accept(BigDecimal number) {
        list.add(number);
    }
    
    public BigDecimalSummaryStatistics combine(BigDecimalSummaryStatistics other) {
        list.addAll(other.list);
        return this;
    }
    
    public BigDecimal getSum() {
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getAverage() {
        return getSum().divide(new BigDecimal(list.size()), 5, RoundingMode.HALF_UP);
    }
    
    public int getCount() {
        return list.size();
    }
}
