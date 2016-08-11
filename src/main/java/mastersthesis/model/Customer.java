package mastersthesis.model;

import java.math.BigDecimal;

public class Customer {
    public final long custkey;
    public final String name;
    public final String address;
    public final Nation nation;
    public final String phone;
    public final BigDecimal acctbal;
    public final String mktsegment;
    public final String comment;
    
    public Customer(long custkey, String name, String address, Nation nation, String phone, BigDecimal acctbal, String mktsegment, String comment) {
        this.custkey = custkey;
        this.name = name;
        this.address = address;
        this.nation = nation;
        this.phone = phone;
        this.acctbal = acctbal;
        this.mktsegment = mktsegment;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Customer customer = (Customer) o;
    
        return custkey == customer.custkey;
    
    }
    
    @Override
    public int hashCode() {
        return (int) (custkey ^ (custkey >>> 32));
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "custkey=" + custkey +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nation=" + nation +
                ", phone='" + phone + '\'' +
                ", acctbal=" + acctbal +
                ", mktsegment='" + mktsegment + '\'' +
                '}';
    }
}
