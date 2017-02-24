package mastersthesis;

import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Utils {
    
    public static Connection newDatabase() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:mem:testdb${new Random().nextInt(1000)};QUERY_CACHE_SIZE=0");
    }
    
    public static void prettyPrint(ResultSet resultSet) throws Exception {
        ResultSetMetaData metadata = resultSet.getMetaData();
        
        while (resultSet.next()) {
            System.out.println(
                    IntStream.rangeClosed(1, metadata.getColumnCount()).mapToObj(
                            columnIdx -> {
                                try {
                                    return metadata.getColumnName(columnIdx) + ' ' + resultSet.getString(columnIdx);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    ).collect(Collectors.joining(", "))
            );
        }
    }
    
    public static void createSchema(Connection connection) {
        System.out.println("Starting import");
        try {
            connection.createStatement().execute("RUNSCRIPT FROM 'dbgen/dss.ddl'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Import finished");
    }
}