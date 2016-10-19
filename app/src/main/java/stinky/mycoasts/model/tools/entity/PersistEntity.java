package stinky.mycoasts.model.tools.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public abstract class PersistEntity {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

}
