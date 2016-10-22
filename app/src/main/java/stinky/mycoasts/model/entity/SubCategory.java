package stinky.mycoasts.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sub_category")
public class SubCategory extends PersistEntity {

    @DatabaseField
    private String name;

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
}
