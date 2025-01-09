package qa.guru.niffler.data.entity.userdata;

import lombok.Getter;
import lombok.Setter;
import qa.guru.niffler.model.CurrencyValues;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class UserEntity implements Serializable {

    private UUID id;

    private String username;

    private CurrencyValues currency;

    private String firstname;

    private String surname;

    private String fullname;

    private byte[] photo;

    private byte[] photoSmall;

}
