package qa.guru.niffler.service;

import guru.qa.niffler.model.UserJson;
import lombok.SneakyThrows;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.dao.UdUserDao;
import qa.guru.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import qa.guru.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import qa.guru.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import qa.guru.niffler.data.entity.auth.AuthAuthorityEntity;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.tpl.DataSources;
import qa.guru.niffler.data.tpl.XaTransactionTemplate;

import java.sql.SQLException;
import java.util.Arrays;


public class UserDbClient {


    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataJdbcUrl()
    );
    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

                    AuthAuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthAuthorityEntity[]::new);

                    authAuthorityDao.createAuthority(authorityEntities);

                    return UserJson.fromEntity(udUserDao.createUser(UserEntity.fromJson(user)), null);
                }
        );
    }
}

