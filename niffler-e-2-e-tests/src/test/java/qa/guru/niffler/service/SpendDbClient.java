package qa.guru.niffler.service;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.impl.CategoryDaoJdbc;
import qa.guru.niffler.data.dao.impl.SpendDaoJdbc;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;

import static qa.guru.niffler.data.Databases.transaction;


public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spendJson) {

        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                        .createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(new SpendDaoJdbc(connection)
                    .createSpend(spendEntity));
        }, CFG.spendJdbcUrl());


    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);

        return transaction(connection -> CategoryJson.fromEntity(new CategoryDaoJdbc(connection).createCategory(categoryEntity)), CFG.spendJdbcUrl());


    }

    public void deleteCategory(CategoryJson categoryJson) {

        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);

        transaction(connection -> new CategoryDaoJdbc(connection).deleteCategory(categoryEntity), CFG.spendJdbcUrl(), 1);
    }

}
