package stinky.mycoasts.model.tools.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.tools.entity.Category;

public class CategoryDAO extends BaseDaoImpl<Category, Integer> {

    public CategoryDAO(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Category> getAllRoles() throws SQLException{
        return this.queryForAll();
    }
}