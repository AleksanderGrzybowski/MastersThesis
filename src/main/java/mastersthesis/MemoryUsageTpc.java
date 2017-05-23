package mastersthesis;

import java.sql.Connection;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static mastersthesis.Utils.createSchemaH2;

public class MemoryUsageTpc {
    
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Please provide dataset size");
            System.exit(1);
        }
    
        String scaleFactor = args[0];
        String type = args[1];
    
        Utils.recreateData(scaleFactor);
        
        Object store = null;
        Connection connection = null;
        
        if (Objects.equals(type, "store")) {
            store = new Store("dbgen");
        } else if (Objects.equals(type, "h2")) {
            connection = Utils.newDatabase("h2");
            createSchemaH2(connection);
        } else {
            System.err.println("Please provide type");
            System.exit(1);
        }
        
        System.out.println(store);
        System.out.println(connection);
        
        sleep(1000); // not sure if needed
        System.gc();
        sleep(1000);
        
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Usage: " + (runtime.totalMemory() - runtime.freeMemory()));
    }
}
