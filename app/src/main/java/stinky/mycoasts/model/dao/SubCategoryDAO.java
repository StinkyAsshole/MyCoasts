package stinky.mycoasts.model.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.SubCategory;

public class SubCategoryDAO extends BaseDaoImpl<SubCategory, Integer> {

    public SubCategoryDAO(ConnectionSource connectionSource, Class<SubCategory> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<SubCategory> getByCategoryId(int categoryId) throws SQLException{
        return this.query(this.queryBuilder().where().eq("category_id", categoryId).prepare());
    }

    public List<SubCategory> getByName(String name) throws SQLException {
        return this.queryForEq("name", name);
    }
}