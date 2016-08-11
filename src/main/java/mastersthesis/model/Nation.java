package mastersthesis.model;

public class Nation {
    
    public final long nationkey;
    public final String name;
    public final Region region;
    public final String comment;
    
    public Nation(long nationkey, String name, Region region, String comment) {
        this.nationkey = nationkey;
        this.name = name;
        this.region = region;
        this.comment = comment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Nation nation = (Nation) o;
        
        return nationkey == nation.nationkey;
    }
    
    @Override
    public int hashCode() {
        return (int) (nationkey ^ (nationkey >>> 32));
    }
    
    @Override
    public String toString() {
        return "Nation{" +
                "nationkey=" + nationkey +
                ", name='" + name + '\'' +
                ", regionId=" + region.regionkey +
                '}';
    }
}
