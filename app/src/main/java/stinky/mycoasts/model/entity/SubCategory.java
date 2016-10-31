package stinky.mycoasts.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "sub_category")
public class SubCategory extends PersistEntity implements Serializable {
    final public static String TABLE_NAME = "sub_category";

    final public static String COLUMN_NAME = "name";
    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    final public static String COLUMN_CATEGORY_ID = "category_id";
    @DatabaseField(canBeNull = false, foreign = true)
    private Category category;

    public SubCategory(){}

    public SubCategory(String name, Category category){
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static class NamedQuery{
        final public static String getAllSubCategoryWithCategory =
                "SELECT sc._id as _id, sc.name as scn, c.name as cn FROM " + TABLE_NAME + " sc JOIN " + Category.TABLE_NAME + " c " +
                        " ON sc." + COLUMN_CATEGORY_ID +" = c."+PersistEntity.COLUMN_ID+
                        " WHERE sc." + COLUMN_NAME + " like ?";
    }
}
