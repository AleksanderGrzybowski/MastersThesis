package masterthesis.tpc;

import mastersthesis.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.createSchemaH2;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class NaturalJoinH2 {
    
    Connection connection;
    
    @Param({"0.01"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        connection = Utils.newDatabase("h2");
        createSchemaH2(connection);
    }
    
    @Benchmark
    public List<ImmutablePair<Long, Long>> sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
               "select\n" +
                       "\to_orderkey, l_orderkey\n" +
                       "from\n" +
                       "\torders,\n" +
                       "\tlineitem\n" +
                       "where\n" +
                       "\to_orderkey = l_orderkey\n"
        ).executeQuery();
    
        List<ImmutablePair<Long, Long>> result = new ArrayList<>();
        
        while (rs.next()) {
            result.add(new ImmutablePair<>(rs.getLong(1), rs.getLong(2)));
        }
        
        System.out.println("H2 count(*) : " + result.size());
        return result;
    }
}

