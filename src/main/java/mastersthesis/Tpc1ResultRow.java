package mastersthesis;

import java.math.BigDecimal;

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
}
