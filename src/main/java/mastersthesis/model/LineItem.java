package mastersthesis.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LineItem {
    public final Order order;
    public final PartSupp partSupp;
    public final int linenumber;
    public final BigDecimal quantity;
    public final BigDecimal extendedPrice;
    public final BigDecimal discount;
    public final BigDecimal tax;
    public final String returnFlag;
    public final String lineStatus;
    public final LocalDate shipDate;
    public final LocalDate commitDate;
    public final LocalDate receiptDate;
    public final String shipInstruct;
    public final String shipMode;
    public final String comment;
    
    public LineItem(Order order, PartSupp partSupp, int linenumber, BigDecimal quantity, BigDecimal extendedPrice, BigDecimal discount, BigDecimal tax, String returnFlag, String lineStatus, LocalDate shipDate, LocalDate receiptDate, LocalDate commitDate, String shipInstruct, String shipMode, String comment) {
        this.order = order;
        this.partSupp = partSupp;
        this.linenumber = linenumber;
        this.quantity = quantity;
        this.extendedPrice = extendedPrice;
        this.discount = discount;
        this.tax = tax;
        this.returnFlag = returnFlag;
        this.lineStatus = lineStatus;
        this.shipDate = shipDate;
        this.receiptDate = receiptDate;
        this.commitDate = commitDate;
        this.shipInstruct = shipInstruct;
        this.shipMode = shipMode;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        LineItem lineItem = (LineItem) o;
        
        if (order != null ? !order.equals(lineItem.order) : lineItem.order != null) return false;
        return partSupp != null ? partSupp.equals(lineItem.partSupp) : lineItem.partSupp == null;
        
    }
    
    @Override
    public int hashCode() {
        return 31 * ((int) order.orderkey) + linenumber;
    }
    
    @Override
    public String toString() {
        return "LineItem{" +
                "orderId=" + order.orderkey +
                ", partSuppId=(" + partSupp.part.partkey + "," + partSupp.supplier.suppkey + ")" +
                ", linenumber=" + linenumber +
                ", quantity=" + quantity +
                ", extendedPrice=" + extendedPrice +
                ", discount=" + discount +
                ", tax=" + tax +
                ", returnFlag='" + returnFlag + '\'' +
                ", lineStatus='" + lineStatus + '\'' +
                ", shipDate=" + shipDate +
                ", commitDate=" + commitDate +
                ", receiptDate=" + receiptDate +
                ", commitDate=" + commitDate +
                ", shipInstruct='" + shipInstruct + '\'' +
                ", shipMode='" + shipMode + '\'' +
                '}';
    }
}
