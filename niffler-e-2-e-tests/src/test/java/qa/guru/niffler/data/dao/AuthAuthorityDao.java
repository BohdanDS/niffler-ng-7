package qa.guru.niffler.data.dao;


import qa.guru.niffler.data.entity.auth.AuthorityEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

    void createAuthority(AuthorityEntity... authAuthorityEntities);

    List<AuthorityEntity> getAuthorityByUserId(UUID id);

    void deleteUserAuthority(UUID id) throws SQLException;

}
