package qa.guru.niffler.service;

import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.repository.AuthUserRepository;
import qa.guru.niffler.data.repository.UdUserRepository;
import qa.guru.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import qa.guru.niffler.data.repository.impl.UdUserRepositoryHibernate;
import qa.guru.niffler.data.tpl.DataSources;
import qa.guru.niffler.data.tpl.XaTransactionTemplate;
import qa.guru.niffler.model.UserJson;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;


public class UserDbClient {


    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UdUserRepository udUserRepository = new UdUserRepositoryHibernate();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);

                    authUserRepository.createUser(authUser);

                    return UserJson.fromEntity(udUserRepository.createUser(userEntity(username)), null);
                }
        );
    }

    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(targetUser.id()).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUserName();
                            AuthUserEntity authUser = authUserEntity(username, "123");
                            authUserRepository.createUser(authUser);

                            UserEntity addressee = udUserRepository.createUser(userEntity(username));
                            udUserRepository.addIncomeInvitation(targetEntity, addressee);
                            return null;
                        }
                );
            }
        }
    }

    void addOutcomeInvitation(UserJson targetUser, int count) {

    }

    void addFriendship(UserJson targetUser, int count) {

    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            System.out.println(ae);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    public Optional<UserEntity> findUserByID(UUID id) {
        return udUserRepository.findById(id);
    }
}

