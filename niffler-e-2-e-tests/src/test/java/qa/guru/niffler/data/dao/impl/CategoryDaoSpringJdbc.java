package qa.guru.niffler.data.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.mapper.CategoryEntityRowMapper;
import qa.guru.niffler.data.tpl.DataSources;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@ParametersAreNonnullByDefault
public class CategoryDaoSpringJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public @Nonnull CategoryEntity createCategory(CategoryEntity categoryEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO category (username, name, archived)"
                            + "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, categoryEntity.getUsername());
            preparedStatement.setString(2, categoryEntity.getName());
            preparedStatement.setBoolean(3, categoryEntity.isArchived());

            return preparedStatement;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        categoryEntity.setId(generatedKey);
        return categoryEntity;
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        try {
            return Optional.ofNullable
                    (jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE id = ?",
                            CategoryEntityRowMapper.instance, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE username = ? AND name = ?",
                        CategoryEntityRowMapper.instance,
                        username, categoryName));
    }

    @Override
    public @Nonnull List<CategoryEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ?",
                CategoryEntityRowMapper.instance,
                username
        );
    }

    @Override
    public @Nonnull List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM category",
                CategoryEntityRowMapper.instance
        );
    }

    @Override
    public @Nonnull CategoryEntity updateCategory(CategoryEntity categoryEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE category SET " +
                            "name = ?, " +
                            "username = ?, " +
                            "archived = ? " +
                            "WHERE id = ?"
            );
            ps.setString(1, categoryEntity.getName());
            ps.setString(2, categoryEntity.getUsername());
            ps.setBoolean(3, categoryEntity.isArchived());
            ps.setObject(4, categoryEntity.getId());

            return ps;
        });
        return categoryEntity;
    }

    @Override
    public void deleteCategory(CategoryEntity categoryEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", categoryEntity.getId());
    }
}
