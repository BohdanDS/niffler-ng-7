package qa.guru.niffler.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DataSources {
    private DataSources() {
    }

    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    public static DataSource dataSource(String jdbcUrl) {
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
                    dataSourceBean.setPoolSize(3);
                    dataSourceBean.setMaxPoolSize(10);
                    return dataSourceBean;
                }
        );
    }
}
