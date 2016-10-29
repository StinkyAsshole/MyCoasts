package stinky.mycoasts.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

public abstract class PersistEntity implements Serializable{
    final public static String COLUMN_ID = "_id";
    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
