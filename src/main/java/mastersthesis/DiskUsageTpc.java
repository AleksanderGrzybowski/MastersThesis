package mastersthesis;

import java.sql.Connection;

import static mastersthesis.Utils.createSchemaMysql;

public class DiskUsageTpc {
    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Please provide dataset size");
            System.exit(1);
        }
        
        String scaleFactor = args[0];
        
        Utils.recreateData(scaleFactor);
        
        Connection connection = Utils.newDatabase("mysql");
        createSchemaMysql(connection);
        
        System.out.println(connection);
    }
}
