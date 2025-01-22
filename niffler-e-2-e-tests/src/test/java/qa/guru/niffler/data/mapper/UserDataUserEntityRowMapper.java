package qa.guru.niffler.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.data.entity.userdata.UserEntity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataUserEntityRowMapper implements RowMapper<UserEntity> {

    public static UserDataUserEntityRowMapper instance = new UserDataUserEntityRowMapper();

    private UserDataUserEntityRowMapper(){

    }
    @Override
    public UserEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
        userEntity.setFullname(resultSet.getString("full_name"));

        return userEntity;

    }
}
