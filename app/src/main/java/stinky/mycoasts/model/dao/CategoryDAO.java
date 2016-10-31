package stinky.mycoasts.model.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.model.entity.Category;

public class CategoryDAO extends BaseDaoImpl<Category, Integer> {

    public CategoryDAO(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Category> getAll() throws SQLException{
        return this.queryForAll();
    }

    public Category getByName(String name) throws SQLException, NotFoundException {
        List<Category> list = queryForEq(Category.COLUMN_NAME, name);
        if (list == null || list.isEmpty()){
            Category category = new Category();
            category.setName(name);
            create(category);
            return category;
        }
        return list.get(0);
    }
}