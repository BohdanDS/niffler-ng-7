package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.auth.AuthAuthorityEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

    void createAuthority(AuthAuthorityEntity authAuthorityEntity);

    List<AuthAuthorityEntity> getAuthorityByUserId(UUID id);

    void deleteUserAuthority(UUID id) throws SQLException;

}
