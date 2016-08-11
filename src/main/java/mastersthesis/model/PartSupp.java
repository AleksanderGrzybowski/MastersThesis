package mastersthesis.model;

import java.math.BigDecimal;

public class PartSupp {
    public final Part part;
    public final Supplier supplier;
    public final int availqty;
    public final BigDecimal supplyCost;
    public final String comment;
    
    public PartSupp(Part part, Supplier supplier, int availqty, BigDecimal supplyCost, String comment) {
        this.part = part;
        this.supplier = supplier;
        this.availqty = availqty;
        this.supplyCost = supplyCost;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        PartSupp partSupp = (PartSupp) o;
        
        if (part != null ? !part.equals(partSupp.part) : partSupp.part != null) return false;
        return supplier != null ? supplier.equals(partSupp.supplier) : partSupp.supplier == null;
    
    }
    
    @Override
    public int hashCode() {
        int result = part != null ? part.hashCode() : 0;
        result = 31 * result + (supplier != null ? supplier.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "PartSupp{" +
                "partId=" + part.partkey +
                ", supplierId=" + supplier.suppkey +
                ", availqty=" + availqty +
                ", supplyCost=" + supplyCost +
                '}';
    }
}
