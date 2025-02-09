package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import qa.guru.niffler.data.dao.impl.SpendDaoSpringJdbc;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.repository.SpendRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private final SpendDao spendDaoSpringJdbc = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDaoSpringJdbc = new CategoryDaoSpringJdbc();


    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        Optional<CategoryEntity> category = categoryDaoSpringJdbc.findCategoryById(spend.getCategory().getId());

        if (category.isEmpty()) {
            spend.setCategory(categoryDaoSpringJdbc.createCategory(spend.getCategory()));
        }
        return spendDaoSpringJdbc.createSpend(spend);
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        categoryDaoSpringJdbc.updateCategory(spend.getCategory());
        spendDaoSpringJdbc.updateSpend(spend);
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDaoSpringJdbc.createCategory(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDaoSpringJdbc.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDaoSpringJdbc.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDaoSpringJdbc.findSpendById(id);
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return spendDaoSpringJdbc.findByUsernameAndSpendDescription(username, description);
    }

    @Override
    public List<SpendEntity> findSpendByUsername(String username) {
        return spendDaoSpringJdbc.findAllByUsername(username);
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        spendDaoSpringJdbc.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDaoSpringJdbc.deleteCategory(category);
    }
}
