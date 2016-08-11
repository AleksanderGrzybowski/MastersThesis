package mastersthesis.model;

import java.math.BigDecimal;
import java.util.Objects;

public class PartSupp {
    public final String id;
    public final Part part;
    public final Supplier supplier;
    public final int availqty;
    public final BigDecimal supplyCost;
    public final String comment;
    
    public PartSupp(Part part, Supplier supplier, int availqty, BigDecimal supplyCost, String comment) {
        // this is needed, cause schema does not have a key
        // and LineItem table must have indexed access to this class
        this.id = "" + part.partkey + "," + supplier.suppkey;
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
    
        return Objects.equals(id, partSupp.id);
    
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return "PartSupp{" +
                "id=" + id +
                ", part=" + part +
                ", supplier=" + supplier +
                ", availqty=" + availqty +
                ", supplyCost=" + supplyCost +
                '}';
    }
}
