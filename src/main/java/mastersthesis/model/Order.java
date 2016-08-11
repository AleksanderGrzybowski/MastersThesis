package mastersthesis.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    public final long orderkey;
    public final Customer customer;
    public final String orderstatus;
    public final BigDecimal totalPrice;
    public final LocalDate orderDate;
    public final String orderPriority;
    public final String clerk;
    public final int shipPriority;
    public final String comment;
    
    public Order(long orderkey, Customer customer, String orderstatus, BigDecimal totalPrice, LocalDate orderDate, String orderPriority, String clerk, int shipPriority, String comment) {
        this.orderkey = orderkey;
        this.customer = customer;
        this.orderstatus = orderstatus;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderPriority = orderPriority;
        this.clerk = clerk;
        this.shipPriority = shipPriority;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Order order = (Order) o;
    
        return orderkey == order.orderkey;
    
    }
    
    @Override
    public int hashCode() {
        return (int) (orderkey ^ (orderkey >>> 32));
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderkey=" + orderkey +
                ", customerId=" + customer.custkey +
                ", orderstatus='" + orderstatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", orderPriority='" + orderPriority + '\'' +
                ", clerk='" + clerk + '\'' +
                ", shipPriority=" + shipPriority +
                '}';
    }
}
