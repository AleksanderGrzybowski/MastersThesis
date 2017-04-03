package mastersthesis;

public class Tpc4ResultRow {
    
    public String orderPriority;
    public long orderCount;
    
    public Tpc4ResultRow(String orderPriority, long orderCount) {
        this.orderPriority = orderPriority;
        this.orderCount = orderCount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Tpc4ResultRow that = (Tpc4ResultRow) o;
        
        if (orderCount != that.orderCount) return false;
        return orderPriority.equals(that.orderPriority);
    }
    
    @Override
    public int hashCode() {
        int result = orderPriority.hashCode();
        result = 31 * result + (int) (orderCount ^ (orderCount >>> 32));
        return result;
    }
    
    @Override
    public String toString() {
        return "Tpc4ResultRow{" +
                "orderPriority='" + orderPriority + '\'' +
                ", orderCount=" + orderCount +
                '}';
    }
}
