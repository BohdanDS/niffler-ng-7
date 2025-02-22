package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.spend.CategoryEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@ParametersAreNonnullByDefault
public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    CategoryEntityRowMapper() {

    }

    @Override
    public @Nonnull CategoryEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(resultSet.getObject("id", UUID.class));
        categoryEntity.setUsername(resultSet.getString("username"));
        categoryEntity.setName(resultSet.getString("name"));
        categoryEntity.setArchived(resultSet.getBoolean("archived"));
        return categoryEntity;
    }
}
