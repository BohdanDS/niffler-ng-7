package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    SpendEntityRowMapper() {

    }

    @Override
    public @Nonnull SpendEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();
        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
        spendEntity.setId(resultSet.getObject("id", UUID.class));
        spendEntity.setUsername(resultSet.getString("username"));
        spendEntity.setSpendDate(resultSet.getDate("spend_date"));
        spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        spendEntity.setAmount(resultSet.getDouble("amount"));
        spendEntity.setDescription(resultSet.getString("description"));
        spendEntity.setCategory(categoryEntity);

        return spendEntity;
    }
}
