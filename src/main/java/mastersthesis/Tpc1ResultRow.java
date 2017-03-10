package mastersthesis;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Tpc1ResultRow {
    
    public String l_returnflag, l_linestatus;
    public BigDecimal sum_qty, sum_base_price, sum_disc_price, sum_charge, avg_qty, avg_price, avg_disc;
    public long count_order;
    
    @Override
    public String toString() {
        return "Tpc1ResultRow{" +
                "l_returnflag='" + l_returnflag + '\'' +
                ", l_linestatus='" + l_linestatus + '\'' +
                ", sum_qty=" + sum_qty +
                ", sum_base_price=" + sum_base_price +
                ", sum_disc_price=" + sum_disc_price +
                ", sum_charge=" + sum_charge +
                ", avg_qty=" + avg_qty +
                ", avg_price=" + avg_price +
                ", avg_disc=" + avg_disc +
                ", count_order=" + count_order +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Tpc1ResultRow that = (Tpc1ResultRow) o;
        
        if (l_returnflag != null ? !l_returnflag.equals(that.l_returnflag) : that.l_returnflag != null) return false;
        if (l_linestatus != null ? !l_linestatus.equals(that.l_linestatus) : that.l_linestatus != null) return false;
        
        if (count_order != that.count_order) return false;
        
        if (!sum_qty.setScale(2, RoundingMode.HALF_UP).equals(that.sum_qty.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!sum_base_price.setScale(2, RoundingMode.HALF_UP).equals(that.sum_base_price.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!sum_disc_price.setScale(2, RoundingMode.HALF_UP).equals(that.sum_disc_price.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!sum_charge.setScale(2, RoundingMode.HALF_UP).equals(that.sum_charge.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!avg_qty.setScale(2, RoundingMode.HALF_UP).equals(that.avg_qty.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!avg_price.setScale(2, RoundingMode.HALF_UP).equals(that.avg_price.setScale(2, RoundingMode.HALF_UP)))
            return false;
        if (!avg_disc.setScale(2, RoundingMode.HALF_UP).equals(that.avg_disc.setScale(2, RoundingMode.HALF_UP)))
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
}
