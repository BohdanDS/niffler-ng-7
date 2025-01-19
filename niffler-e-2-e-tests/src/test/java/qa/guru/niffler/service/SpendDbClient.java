package qa.guru.niffler.service;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.dao.impl.CategoryDaoJdbc;
import qa.guru.niffler.data.dao.impl.SpendDaoJdbc;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.tpl.JdbcTransactionTemplate;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;


public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.createSpend(spendEntity)
                    );
                }
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> CategoryJson.fromEntity(categoryDao.createCategory(CategoryEntity.fromJson(category))));
    }

    public void deleteCategory(CategoryJson categoryJson){
        jdbcTxTemplate.execute(()-> {
            categoryDao.deleteCategory(CategoryEntity.fromJson(categoryJson));
            return null;
        });
    }
}
