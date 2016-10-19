package stinky.mycoasts.model.tools.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sub_category")
public class SubCategory extends PersistEntity {

    @DatabaseField
    private String name;

    @DatabaseField(canBeNull = false, foreign = true)
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
