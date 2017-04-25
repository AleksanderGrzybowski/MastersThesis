package mastersthesis;

public class JavaMain {
    
    public static void main(String[] args) throws Exception {
        Utils.createSchemaMysql(Utils.newDatabase("mysql"));
    }
}
