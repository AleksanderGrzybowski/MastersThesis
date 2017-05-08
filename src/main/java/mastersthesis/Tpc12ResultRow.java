package mastersthesis;

public class Tpc12ResultRow {
    
    public String l_shipmode;
    public int high_line_count, low_line_count;
    
    public Tpc12ResultRow(String l_shipmode, int high_line_count, int low_line_count) {
        this.l_shipmode = l_shipmode;
        this.high_line_count = high_line_count;
        this.low_line_count = low_line_count;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Tpc12ResultRow that = (Tpc12ResultRow) o;
        
        if (high_line_count != that.high_line_count) return false;
        if (low_line_count != that.low_line_count) return false;
        return l_shipmode != null ? l_shipmode.equals(that.l_shipmode) : that.l_shipmode == null;
    }
    
    @Override
    public int hashCode() {
        int result = l_shipmode != null ? l_shipmode.hashCode() : 0;
        result = 31 * result + high_line_count;
        result = 31 * result + low_line_count;
        return result;
    }
    
    @Override
    public String toString() {
        return "Tpc12ResultRow{" +
                "l_shipmode='" + l_shipmode + '\'' +
                ", high_line_count=" + high_line_count +
                ", low_line_count=" + low_line_count +
                '}';
    }
}
