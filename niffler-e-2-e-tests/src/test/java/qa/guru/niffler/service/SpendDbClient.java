package qa.guru.niffler.service;

import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.dao.impl.CategoryDaoJdbc;
import qa.guru.niffler.data.dao.impl.SpendDaoJdbc;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.model.SpendJson;


public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }
}
