package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    SpendEntity createSpend(SpendEntity spendEntity);

    Optional<SpendEntity> findSpendById(UUID id);

    List<SpendEntity> findAllByUsername(String username);

    SpendEntity updateSpend(SpendEntity spendEntity);

    List<SpendEntity> findAll();

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void deleteSpend(SpendEntity spendEntity);
}
