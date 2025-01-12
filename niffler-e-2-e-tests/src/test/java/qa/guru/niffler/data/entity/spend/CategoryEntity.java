package qa.guru.niffler.data.entity.spend;

import lombok.Getter;
import lombok.Setter;
import qa.guru.niffler.model.CategoryJson;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CategoryEntity implements Serializable {

    private UUID id;

    private String name;

    private String username;

    private boolean archived;

    public static CategoryEntity fromJson(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryJson.id());
        categoryEntity.setName(categoryJson.name());
        categoryEntity.setUsername(categoryJson.username());
        categoryEntity.setArchived(categoryJson.archived());
        return categoryEntity;
    }
}