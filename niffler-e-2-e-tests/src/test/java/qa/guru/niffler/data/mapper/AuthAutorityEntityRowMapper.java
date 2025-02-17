package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthAutorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthAutorityEntityRowMapper instance = new AuthAutorityEntityRowMapper();

    private AuthAutorityEntityRowMapper() {
    }

    @Override
    public @Nonnull AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authorityEntity.setId(rs.getObject("id", UUID.class));
        authUserEntity.setId(rs.getObject("user_id", UUID.class));
        authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
        authorityEntity.setUser(authUserEntity);

        return authorityEntity;
    }
}
