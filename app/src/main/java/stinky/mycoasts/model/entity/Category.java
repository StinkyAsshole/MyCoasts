package stinky.mycoasts.model.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "category")
public class Category extends PersistEntity {

    @DatabaseField
    private String name;

    @ForeignCollectionField
    private ForeignCollection<SubCategory> subCategories;

    public Category(){}

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollection<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ForeignCollection<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
