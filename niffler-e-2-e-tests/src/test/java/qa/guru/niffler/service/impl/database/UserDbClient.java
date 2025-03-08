package qa.guru.niffler.service.impl.database;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import qa.guru.niffler.data.tpl.XaTransactionTemplate;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.UserClient;

import java.util.Arrays;
import java.util.List;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;


public class UserDbClient implements UserClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UdUserRepository udUserRepository = new UdUserRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataJdbcUrl()
    );

    @Override
    public UserJson createUser(String username, String password) {
        return xaTxTemplate.execute(() -> {
            AuthUserEntity authUser = authUserEntity(username, password);

            authUserRepository.createUser(authUser);

            return UserJson.fromEntity(
                    udUserRepository.createUser(userEntity(username)), null);
        });
    }

    @Override
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(targetUser.id()).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String username = randomUserName();
                    String password = "123";

                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.createUser(authUser);
                    UserEntity addressee = udUserRepository.createUser(userEntity(username));

                    udUserRepository.addIncomeInvitation(targetEntity, addressee);
                    return null;
                });
            }
        }
        return null;
    }

    @Override
    public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(targetUser.id()).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String username = randomUserName();
                    String password = "123";

                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.createUser(authUser);
                    UserEntity addressee = udUserRepository.createUser(userEntity(username));

                    udUserRepository.addOutcomeInvitation(addressee, targetEntity);
                    return null;
                });
            }
        }
//        Заглушка
        return null;
    }

    @Override
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(targetUser.id()).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    String username = randomUserName();
                    String password = "12345";

                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.createUser(authUser);
                    UserEntity addressee = udUserRepository.createUser(userEntity(username));

                    udUserRepository.addFriendship(targetEntity, addressee);
                    return null;
                });
            }
        }
        return null;
    }

    @Override
    public List<UserJson> getUsers(@Nullable String username, @Nullable String searchQuery) {
        return null;
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
                Arrays.stream(Authority.values()).map(e -> {
                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setUser(authUser);
                    authority.setAuthority(e);
                    return authority;
                }).toList()
        );
        return authUser;
    }
}

