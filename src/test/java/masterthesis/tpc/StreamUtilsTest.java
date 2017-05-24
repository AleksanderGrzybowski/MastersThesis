package masterthesis.tpc;

import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class StreamUtilsTest {
    
    final List<Customer> CUSTOMERS = asList(
            new Customer(1, "customerA"),
            new Customer(2, "customerB"),
            new Customer(3, "customerC"),
            new Customer(4, "customerD")
    );
    
    final List<Order> ORDERS = asList(
            new Order(1, 1, 100),
            new Order(2, 1, 200),
            new Order(3, 4, 300),
            new Order(4, 3, 400)
    );
    
    @Test
    public void should_do_equivalent_operations() {
        Set flatMapResult = StreamUtils.innerJoin(
                CUSTOMERS,
                ORDERS,
                pair -> pair.left.id == pair.right.customerId
        ).collect(Collectors.toSet());
        Set hashMapResult = StreamUtils.innerJoinHashmaps(
                CUSTOMERS,
                customer -> customer.id,
                ORDERS,
                order -> order.customerId
        ).collect(Collectors.toSet());
    
        System.out.println(flatMapResult);
        System.out.println(hashMapResult);
        assertEquals(flatMapResult, hashMapResult);
    }
}

class Customer {
    public final int id;
    public final String name;
    
    Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Customer customer = (Customer) o;
        
        if (id != customer.id) return false;
        return name.equals(customer.name);
    }
    
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

class Order {
    public final int orderId;
    public final int customerId;
    public final long price;
    
    Order(int orderId, int customerId, long price) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Order order = (Order) o;
        
        if (orderId != order.orderId) return false;
        if (customerId != order.customerId) return false;
        return price == order.price;
    }
    
    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + customerId;
        result = 31 * result + (int) (price ^ (price >>> 32));
        return result;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", price=" + price +
                '}';
    }
}
