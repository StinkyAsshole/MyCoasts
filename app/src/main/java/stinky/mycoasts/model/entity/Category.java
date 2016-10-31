package stinky.mycoasts.model.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "category")
public class Category extends PersistEntity implements Serializable {
    final public static String TABLE_NAME = "category";

    final public static String COLUMN_NAME = "name";
    @DatabaseField(columnName = COLUMN_NAME)
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

    public static class NamedQuery{
        final public static String getAllSubCategoryWithCategory =
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " like ?";
    }
}
