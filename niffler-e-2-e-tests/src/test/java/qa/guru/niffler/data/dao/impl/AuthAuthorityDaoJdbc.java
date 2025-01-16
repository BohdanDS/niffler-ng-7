package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.entity.auth.AuthAuthorityEntity;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void createAuthority(AuthAuthorityEntity... authAuthorityEntities) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority)" +
                        "VALUES(?, ?)"
        )) {

            for (AuthAuthorityEntity authorityEntity : authAuthorityEntities){
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
    public List<AuthAuthorityEntity> getAuthorityByUserId(UUID id) {
        List<AuthAuthorityEntity> authAuthorityEntities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM \"authority\" where user_id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    AuthAuthorityEntity authAuthorityEntity = new AuthAuthorityEntity();

                    authAuthorityEntity.setId(resultSet.getObject("id", UUID.class));
                    authAuthorityEntity.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                    authAuthorityEntity.setUser(new AuthUserEntity(resultSet.getObject("user_id", UUID.class)));
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM \"authority\" WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        }
    }
}
