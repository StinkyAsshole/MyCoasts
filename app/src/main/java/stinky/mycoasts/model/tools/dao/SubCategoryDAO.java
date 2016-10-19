package stinky.mycoasts.model.tools.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.tools.entity.Category;
import stinky.mycoasts.model.tools.entity.SubCategory;

public class SubCategoryDAO extends BaseDaoImpl<SubCategory, Integer> {

    public SubCategoryDAO(ConnectionSource connectionSource, Class<SubCategory> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<SubCategory> getAllRoles() throws SQLException{
        return this.queryForAll();
    }
}