package mastersthesis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Utils {
    
    public static Connection newDatabase() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:mem:testdb" + new Random().nextInt(1000) + ";QUERY_CACHE_SIZE=0");
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