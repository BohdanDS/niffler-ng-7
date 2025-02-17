package qa.guru.niffler.data.tpl;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@ParametersAreNonnullByDefault
public class JdbcConnectionHolders implements AutoCloseable {

    private final List<JdbcConnectionHolder> holders;

    public JdbcConnectionHolders(List<JdbcConnectionHolder> holders) {
        this.holders = holders;
    }

    @Override
    public void close() {
        holders.forEach(JdbcConnectionHolder::close);
    }
}
