package qa.guru.niffler.data.tpl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@ParametersAreNonnullByDefault
public class Connections {
    private Connections() {
    }

    private static final Map<String, JdbcConnectionHolder> holder = new ConcurrentHashMap<>();

    public static @Nonnull JdbcConnectionHolder holder(String jdbcUrl) {
        return holder.computeIfAbsent(
                jdbcUrl,
                key -> new JdbcConnectionHolder(DataSources.dataSource(jdbcUrl))
        );
    }

    public static @Nonnull JdbcConnectionHolders holders(String... jdbcUrl) {
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
