package mastersthesis

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData

@SuppressWarnings("SqlNoDataSourceInspection")
class Main {
    
    static Connection newDatabase() throws Exception {
        Class.forName("org.h2.Driver")
        return DriverManager.getConnection("jdbc:h2:mem:testdb${new Random().nextInt(1000)};QUERY_CACHE_SIZE=0")
    }

    static void prettyPrint(ResultSet resultSet) {
        ResultSetMetaData metadata = resultSet.metaData

        while (resultSet.next()) {
            println(
                    (1..metadata.columnCount).collect { int columnIdx ->
                        resultSet.getString(columnIdx) + " " + metadata.getColumnName(columnIdx)
                    }.join(",  ")
            )
        }
    }

    static void createSchema(Connection connection) {
        connection.createStatement().execute("RUNSCRIPT FROM 'dbgen/dss.ddl'")
    }

    public static void main(String[] args) {
//        new Store("dbgen")
    }
}