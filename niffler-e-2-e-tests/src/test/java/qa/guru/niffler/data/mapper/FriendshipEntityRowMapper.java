package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.userdata.FriendshipEntity;
import qa.guru.niffler.data.entity.userdata.FriendshipStatus;
import qa.guru.niffler.data.entity.userdata.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@ParametersAreNonnullByDefault
public class FriendshipEntityRowMapper implements RowMapper<FriendshipEntity> {

    public static final FriendshipEntityRowMapper instance = new FriendshipEntityRowMapper();
    public FriendshipEntityRowMapper() {
    }

    @Override
    public @Nonnull FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(rs.getObject("requester_id", UserEntity.class));
        friendshipEntity.setAddressee(rs.getObject("addressee_id", UserEntity.class));
        friendshipEntity.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
        friendshipEntity.setCreatedDate(rs.getDate("created_date"));
        return friendshipEntity;
    }
}
