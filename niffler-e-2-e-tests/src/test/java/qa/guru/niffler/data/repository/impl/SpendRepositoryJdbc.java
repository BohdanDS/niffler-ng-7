package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.dao.impl.CategoryDaoJdbc;
import qa.guru.niffler.data.dao.impl.SpendDaoJdbc;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.repository.SpendRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();


    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        Optional<CategoryEntity> category = categoryDao.findCategoryById(spend.getCategory().getId());

        if (category.isEmpty()) {
            spend.setCategory(categoryDao.createCategory(spend.getCategory()));
        }

        return spendDao.createSpend(spend);
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        categoryDao.updateCategory(spend.getCategory());
        spendDao.updateSpend(spend);
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.createCategory(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return spendDao.findByUsernameAndSpendDescription(username, description);
    }

    @Override
    public List<SpendEntity> findSpendByUsername(String username) {
        return spendDao.findAllByUsername(username);
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
