package qa.guru.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
    private Databases() {
    }

    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Connection>> threadConnection = new ConcurrentHashMap<>();

    public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl, int isolationLevel) {
    }

    public record XaConsumer(Consumer<Connection> function, String jdbcUrl) {
    }


    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            T result = function.apply(connection);
            connection.commit();
            connection.setAutoCommit(true);
            return result;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl, int isolationLevel) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(isolationLevel);
            consumer.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public static <T> T xaTransaction(XaFunction<T>... actions) {
        UserTransaction userTransaction = new UserTransactionImp();

        try {
            userTransaction.begin();
            T result = null;
            for (XaFunction<T> action : actions) {
                try (Connection connection = connection(action.jdbcUrl)) {
                    connection.setAutoCommit(false);
                    connection.setTransactionIsolation(action.isolationLevel);
                }
                result = action.function.apply(connection(action.jdbcUrl));
            }

            userTransaction.commit();
            return result;
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void xaConsumer(XaConsumer... actions) {
        UserTransaction userTransaction = new UserTransactionImp();

        try {
            userTransaction.begin();
            for (XaConsumer action : actions) {
                action.function.accept(connection(action.jdbcUrl));
            }

            userTransaction.commit();

        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private static DataSource dataSource(String jdbcUrl) {
        return dataSources.computeIfAbsent(
                jdbcUrl,
                kye -> {

                    AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
                    final String uniqId = StringUtils.substringAfter(jdbcUrl, "5432/");
                    dataSourceBean.setUniqueResourceName(uniqId);
                    dataSourceBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
                    Properties properties = new Properties();
                    properties.put("URL", jdbcUrl);
                    properties.put("user", "postgres");
                    properties.put("password", "secret");
                    dataSourceBean.setXaProperties(properties);
                    dataSourceBean.setMaxPoolSize(10);
                    return dataSourceBean;
                }
        );
    }

    public static Connection connection(String jdbcUrl) {
        return threadConnection.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        return new HashMap<>(Map.of(jdbcUrl, dataSource(jdbcUrl).getConnection()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).computeIfAbsent(
                jdbcUrl,
                key -> {
                    try {
                        return dataSource(jdbcUrl).getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static void closeAllConnections() {
        for (Map<String, Connection> connectionMap : threadConnection.values()) {
            for (Connection connection : connectionMap.values()) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ignored) {
//                    NOP
                }
            }
        }

    }
}
