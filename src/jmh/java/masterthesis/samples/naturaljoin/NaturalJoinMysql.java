package masterthesis.samples.naturaljoin;

import mastersthesis.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openjdk.jmh.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static mastersthesis.Utils.createSchemaMysql;

@SuppressWarnings("SqlResolve")
@State(Scope.Thread)
public class NaturalJoinMysql {
    
    Connection connection;
    
    @Param({"0.010", "0.025", "0.050", "0.075", "0.100", "0.125", "0.150", "0.175", "0.200"})
    public String scaleFactor;
    
    @Setup
    public void setup() throws Exception {
        Utils.recreateData(scaleFactor);
        connection = Utils.newDatabase("mysql");
        createSchemaMysql(connection);
    }
    
    @Benchmark
    public List<ImmutablePair<Long, Long>> sql() throws Exception {
        ResultSet rs = connection.prepareStatement(
               "select\n" +
                       "\tO_ORDERKEY, L_ORDERKEY\n" +
                       "from\n" +
                       "\tORDERS,\n" +
                       "\tLINEITEM\n" +
                       "where\n" +
                       "\tO_ORDERKEY = L_ORDERKEY\n"
        ).executeQuery();
    
        List<ImmutablePair<Long, Long>> result = new ArrayList<>();
        
        while (rs.next()) {
            result.add(new ImmutablePair<>(rs.getLong(1), rs.getLong(2)));
        }
        
        System.out.println("Mysql count(*) : " + result.size());
        return result;
    }
}

