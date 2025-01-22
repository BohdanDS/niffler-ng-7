package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.entity.auth.Authority;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static qa.guru.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void createAuthority(AuthorityEntity... authAuthorityEntities) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority)" +
                        "VALUES(?, ?)"
        )) {

            for (AuthorityEntity authorityEntity : authAuthorityEntities) {
                preparedStatement.setObject(1, authorityEntity.getUser().getId());
                preparedStatement.setString(2, authorityEntity.getAuthority().name());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> getAuthorityByUserId(UUID id) {
        List<AuthorityEntity> authAuthorityEntities = new ArrayList<>();

        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"authority\" where user_id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    AuthorityEntity authAuthorityEntity = new AuthorityEntity();

                    authAuthorityEntity.setId(resultSet.getObject("id", UUID.class));
                    authAuthorityEntity.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                    authAuthorityEntity.getUser().setId(resultSet.getObject("user_id", UUID.class));
                    authAuthorityEntities.add(authAuthorityEntity);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return authAuthorityEntities;
    }

    @Override
    public void deleteUserAuthority(UUID id) throws SQLException {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"authority\" WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        }
    }
}
