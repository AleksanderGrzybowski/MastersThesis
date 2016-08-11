package mastersthesis.model;

import java.math.BigDecimal;

public class Part {
    public final long partkey;
    public final String name;
    public final String mfgr;
    public final String brand;
    public final String type;
    public final int size;
    public final String container;
    public final BigDecimal retailPrice;
    public final String comment;
    
    public Part(long partkey, String name, String mfgr, String brand, String type, int size, String container, BigDecimal retailPrice, String comment) {
        this.partkey = partkey;
        this.name = name;
        this.mfgr = mfgr;
        this.brand = brand;
        this.type = type;
        this.size = size;
        this.container = container;
        this.retailPrice = retailPrice;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Part part = (Part) o;
    
        return partkey == part.partkey;
    
    }
    
    @Override
    public int hashCode() {
        return (int) (partkey ^ (partkey >>> 32));
    }
    
    @Override
    public String toString() {
        return "Part{" +
                "partkey=" + partkey +
                ", name='" + name + '\'' +
                ", mfgr='" + mfgr + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", container='" + container + '\'' +
                ", retailPrice=" + retailPrice +
                '}';
    }
}
