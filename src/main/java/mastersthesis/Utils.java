package mastersthesis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Utils {
    
    private static Random random = new Random();
    
    public static Connection newDatabase(String databaseVendor) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("org.h2.Driver");
    
        if (Objects.equals(databaseVendor, "mysql")) {
            // docker run --rm -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=test -p 127.0.0.1:7777:3306 -ti mysql
            String databaseName = "test" + random.nextInt(10000);
            DriverManager.getConnection(
                    "jdbc:mysql://localhost:7777/",
                    "root",
                    "password"
            ).createStatement().executeUpdate("create database " + databaseName);
            
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:7777/" + databaseName,
                    "root",
                    "password"
            );
        } else if (Objects.equals(databaseVendor, "h2")) {
            return DriverManager.getConnection("jdbc:h2:mem:testdb" + random.nextInt(1000) + ";QUERY_CACHE_SIZE=0");
        } else if (Objects.equals(databaseVendor, "hsqldb")) {
            return DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb" + random.nextInt(1000), "SA", "");
        } else {
            throw new AssertionError("No such database vendor: " + databaseVendor);
        }
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
    
    public static void recreateData(String scaleFactor) throws Exception {
        System.out.println("Recreating data, scaleFactor = " + scaleFactor + "...");
        
        Process process = new ProcessBuilder(
                asList("/mnt/Dysk/Kodzenie/MastersThesis/dbgen/dbgenw.sh", "-v", "-f", "-s", scaleFactor)
        )
                .directory(new File("/mnt/Dysk/Kodzenie/MastersThesis/dbgen"))
                .start();
        process.waitFor();
        
        consumeAndPrintOutput(process);
        
        if (process.exitValue() != 0) {
            throw new RuntimeException("Process finished with error code " + process.exitValue());
        } else {
            System.out.println("Recreating finished!");
        }
    }
    
    private static void consumeAndPrintOutput(Process process) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        
        String line;
        while ((line = stdInput.readLine()) != null) {
            System.out.println(line);
        }
        
        while ((line = stdError.readLine()) != null) {
            System.out.println(line);
        }
    }
}