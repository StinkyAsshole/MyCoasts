package stinky.mycoasts.model.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.entity.Account;

public class AccountDAO extends BaseDaoImpl<Account, Integer> {

    public AccountDAO(ConnectionSource connectionSource, Class<Account> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Account> getAll() throws SQLException{
        return this.queryForAll();
    }

}