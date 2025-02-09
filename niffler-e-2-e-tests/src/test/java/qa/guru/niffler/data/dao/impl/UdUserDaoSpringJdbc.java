package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.UdUserDao;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.mapper.UserDataUserEntityRowMapper;
import qa.guru.niffler.data.tpl.DataSources;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoSpringJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity userEntity) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)"
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, userEntity.getUsername());
            preparedStatement.setString(2, userEntity.getCurrency().name());
            preparedStatement.setString(3, userEntity.getFirstname());
            preparedStatement.setObject(4, userEntity.getSurname());
            preparedStatement.setBytes(5, userEntity.getPhoto());
            preparedStatement.setBytes(6, userEntity.getPhotoSmall());
            preparedStatement.setString(7, userEntity.getFullname());

            return preparedStatement;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        userEntity.setId(generatedKey);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        jdbcTemplate.update("""
                                      UPDATE "user"
                                        SET currency    = ?,
                                            firstname   = ?,
                                            surname     = ?,
                                            photo       = ?,
                                            photo_small = ?
                                        WHERE id = ?
                        """,
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getId());

        jdbcTemplate.batchUpdate("""
                                         INSERT INTO friendship (requester_id, addressee_id, status)
                                         VALUES (?, ?, ?)
                                         ON CONFLICT (requester_id, addressee_id)
                                             DO UPDATE SET status = ?
                        """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, user.getId());
                        ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
                        ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
                        ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getFriendshipRequests().size();
                    }
                });
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserDataUserEntityRowMapper.instance, userId));
    }

    @Override
    public Optional<UserEntity> findByUsername(String userName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserDataUserEntityRowMapper.instance, userName));
    }

    @Override
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" WHERE id = ?",
                UserDataUserEntityRowMapper.instance);
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", userEntity.getId());
    }
}
