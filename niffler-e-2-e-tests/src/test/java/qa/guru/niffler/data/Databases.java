package qa.guru.niffler.data;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Databases {
    private Databases() {
    }

    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    private static DataSource dataSource(String jdbcUrl) {
        return dataSources.computeIfAbsent(
                jdbcUrl,
                kye -> {
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setUser("postgres");
                    pgSimpleDataSource.setPassword("secret");
                    pgSimpleDataSource.setURL(kye);
                    return pgSimpleDataSource;
                }
        );
    }

    public static Connection connection (String jdbcUrl) throws SQLException {
        return dataSource(jdbcUrl).getConnection();
    }
}
