package qa.guru.niffler.data.tpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connections {
    private Connections() {
    }

    private static final Map<String, JdbcConnectionHolder> holder = new ConcurrentHashMap<>();

    public static JdbcConnectionHolder holder(String jdbcUrl) {
        return holder.computeIfAbsent(
                jdbcUrl,
                key -> new JdbcConnectionHolder(DataSources.dataSource(jdbcUrl))
        );
    }

    public static JdbcConnectionHolders holders(String... jdbcUrl) {
        List<JdbcConnectionHolder> result = new ArrayList<>();
        for (String url : jdbcUrl) {
            result.add(holder(url));
        }
        return new JdbcConnectionHolders(result);
    }

    public static void closeAllConnections() {
        holder.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }
}
