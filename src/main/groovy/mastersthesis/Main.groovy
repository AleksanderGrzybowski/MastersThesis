package mastersthesis

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData

@SuppressWarnings("SqlNoDataSourceInspection")
class Main {
    private static Connection createDatabase(String name) throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:mem:" + name);
    }

    static void prettyPrint(ResultSet resultSet) {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }


    @SuppressWarnings("SqlDialectInspection")
    public static void main(String[] args) {
        Connection connection = createDatabase("kelogdb")

        def statement = connection.createStatement()

        def rs = statement.execute("RUNSCRIPT FROM 'tpc/dss.ddl'")

        rs = statement.executeQuery("select\n" +
                "\tl_returnflag,\n" +
                "\tl_linestatus,\n" +
                "\tsum(l_quantity) as sum_qty,\n" +
                "\tsum(l_extendedprice) as sum_base_price,\n" +
                "\tsum(l_extendedprice * (1 - l_discount)) as sum_disc_price,\n" +
                "\tsum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,\n" +
                "\tavg(l_quantity) as avg_qty,\n" +
                "\tavg(l_extendedprice) as avg_price,\n" +
                "\tavg(l_discount) as avg_disc,\n" +
                "\tcount(*) as count_order\n" +
                "from\n" +
                "\tlineitem\n" +
                "where\n" +
                "\tl_shipdate <= date '1998-12-01' " +
                "group by\n" +
                "\tl_returnflag,\n" +
                "\tl_linestatus\n" +
                "order by\n" +
                "\tl_returnflag,\n" +
                "\tl_linestatus;")

        prettyPrint(rs)


    }
}