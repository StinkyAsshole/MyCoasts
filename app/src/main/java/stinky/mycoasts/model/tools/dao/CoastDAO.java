package stinky.mycoasts.model.tools.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import stinky.mycoasts.model.tools.entity.Account;
import stinky.mycoasts.model.tools.entity.Coast;

public class CoastDAO extends BaseDaoImpl<Coast, Integer> {

    private long limit = 20;

    public CoastDAO(ConnectionSource connectionSource, Class<Coast> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Coast> getAll() throws SQLException{
        return this.queryForAll();
    }

    public List<Coast> getByDate(Date startDate, Date finishDate, int page) throws SQLException{
        return this.query(
                pageQuery(page).where().ge("date", startDate).and().le("date", finishDate).prepare()
        );
    }

    private QueryBuilder<Coast, Integer> pageQuery(int page) throws SQLException {
        return queryBuilder().limit(limit).offset(page*limit);
    }
}