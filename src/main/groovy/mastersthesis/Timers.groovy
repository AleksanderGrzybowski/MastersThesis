package mastersthesis

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.util.stream.Collectors

@SuppressWarnings("SqlNoDataSourceInspection")
class Timers {

    static int timeIt(Runnable r) {
        long prev = System.nanoTime();
        r.run();
        long next = System.nanoTime()
        return (next - prev) / 1_000_000
    }

    static void perform(Map params) {
        String columns = params.columns
        String sqlSubject = (params.sqlSubject as String).replace("#tablename", "benchtable")
        Closure streamsSubject = params.streamsSubject
        Closure<List<List>> generator = params.data
        List<List> data = null

        int dataTime = timeIt {
            data = generator()
        }
        println "[data] ${dataTime} ms"

        String databaseName = UUID.randomUUID().toString()[0..5]
        Connection connection = createDatabase databaseName

        Statement statement = connection.createStatement()
        statement.execute("CREATE TABLE benchtable " + columns)

        int insertTime = timeIt {
            statement = connection.createStatement()
            data.each { List row -> // for now ints and strings
                String toAppend = row.collect { it instanceof Integer ? it.toString() : ("'" + it + "'") }.join(',')
                String query = "insert into benchtable values ($toAppend)"
                statement.addBatch(query)
            }
            statement.executeBatch()
        }

        println "[ins ] ${insertTime} ms"

        statement = connection.createStatement()

        int sqlTime = timeIt {
            ResultSet rs = statement.executeQuery(sqlSubject)
            while (rs.next());
        }

        int streamsTime = timeIt { streamsSubject(data) }
        println "[sql ] ${sqlTime} ms"
        println "[java] ${streamsTime} ms"


    }

    public static void main(String[] args) {
        perform([
                columns       : "(xx INTEGER)",
                data          : {
                    new Random().ints().limit(1_000_000).boxed().collect(Collectors.toList())
                            .collect { [it] }
                },
                sqlSubject    : "select * from #tablename where xx > ${Integer.MAX_VALUE / 2}",
                streamsSubject: { List<List> data ->
                    data.stream().filter { it[0] > Integer.MAX_VALUE / 2 }
                            .collect(Collectors.toList())
                }
        ])
    }
}