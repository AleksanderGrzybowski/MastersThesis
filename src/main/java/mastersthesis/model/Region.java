package mastersthesis.model;

public class Region {
    public final long regionkey;
    public final String name;
    public final String comment;
    
    public Region(long regionkey, String name, String comment) {
        this.regionkey = regionkey;
        this.name = name;
        this.comment = comment;
    }
    
    @Override
    public String toString() {
        return "Region{" +
                "regionkey=" + regionkey +
                ", name='" + name + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Region region = (Region) o;
    
        return regionkey == region.regionkey;
    
    }
    
    @Override
    public int hashCode() {
        return (int) (regionkey ^ (regionkey >>> 32));
    }
}
