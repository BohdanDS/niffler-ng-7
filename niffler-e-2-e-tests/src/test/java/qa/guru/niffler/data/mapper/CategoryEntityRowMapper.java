package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.spend.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    CategoryEntityRowMapper() {

    }

    @Override
    public CategoryEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(resultSet.getObject("id", UUID.class));
        categoryEntity.setUsername(resultSet.getString("username"));
        categoryEntity.setName(resultSet.getString("name"));
        categoryEntity.setArchived(resultSet.getBoolean("archived"));
        return categoryEntity;
    }
}
