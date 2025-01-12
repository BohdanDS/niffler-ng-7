package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.spend.SpendEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    SpendEntity createSpend(SpendEntity spendEntity);

    Optional<SpendEntity> findEntityById(UUID id) throws SQLException;

    List<SpendEntity> findAllByUsername(String username);

    void deleteSpend(SpendEntity spendEntity);
}
