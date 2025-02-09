package qa.guru.niffler.service;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.repository.SpendRepository;
import qa.guru.niffler.data.repository.impl.SpendRepositoryHibernate;
import qa.guru.niffler.data.tpl.XaTransactionTemplate;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTxTemplate.execute(() ->
                spendRepository.findSpendById(id).map(SpendJson::fromEntity)
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return xaTxTemplate.execute(() -> {
                    List<SpendEntity> allByUsername = spendRepository.findSpendByUsername(username);
                    return allByUsername.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
                }
        );
    }

    public void deleteSpend(SpendJson spend) {
        xaTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    spendRepository.removeSpend(spendEntity);
                    return null;
                }
        );
    }

    public SpendJson createSpend(SpendJson spend) {
        return xaTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
                }
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return xaTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
                }
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return xaTxTemplate.execute(() -> spendRepository
                .findCategoryByUsernameAndSpendName(username, categoryName)
                .map(CategoryJson::fromEntity)

        );
    }

    public void deleteCategory(CategoryJson category) {
        xaTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    spendRepository.removeCategory(categoryEntity);
                    return null;
                }
        );
    }
}
