package mastersthesis.model;

import java.math.BigDecimal;

public class Supplier {
    public final long suppkey;
    public final String name;
    public final String address;
    public final Nation nation;
    public final String phone;
    public final BigDecimal acctbal;
    public final String comment;
    
    public Supplier(long suppkey, String name, String address, Nation nation, String phone, BigDecimal acctbal, String comment) {
        this.suppkey = suppkey;
        this.name = name;
        this.address = address;
        this.nation = nation;
        this.phone = phone;
        this.acctbal = acctbal;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Supplier supplier = (Supplier) o;
    
        return suppkey == supplier.suppkey;
    
    }
    
    @Override
    public int hashCode() {
        return (int) (suppkey ^ (suppkey >>> 32));
    }
    
    @Override
    public String toString() {
        return "Supplier{" +
                "suppkey=" + suppkey +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nationId=" + nation.nationkey +
                ", phone='" + phone + '\'' +
                ", acctbal=" + acctbal +
                '}';
    }
}
