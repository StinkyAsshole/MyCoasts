package stinky.mycoasts.model.tools.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account")
public class Account extends PersistEntity{

    @DatabaseField
    private String name;

    @DatabaseField
    private Integer amount;

    public Account(){}

    public Account(String name){
        this.name = name;
        amount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
