package stinky.mycoasts.model.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;

public class CoastDAO extends BaseDaoImpl<Coast, Integer> {

    private long limit = 40;

    public CoastDAO(ConnectionSource connectionSource, Class<Coast> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Coast> getByDate(int accountId, Date startDate, Date finishDate, int page) throws SQLException{
        return this.query(
                pageQuery(page).where().ge("date", startDate).and().le("date", finishDate).and().eq("account_id", accountId).prepare()
        );
    }

    public List<Coast> getIncomeByDate(int accountId, Date startDate, Date finishDate, int page) throws SQLException{
        return this.query(
                pageQuery(page).where().ge("date", startDate).and().le("date", finishDate).and().gt("amount",0).and().eq("account_id", accountId).prepare()
        );
    }
    public List<Coast> getOutcomeByDate(int accountId, Date startDate, Date finishDate, int page) throws SQLException{
        return this.query(
                pageQuery(page).where().ge("date", startDate).and().le("date", finishDate).and().lt("amount",0).and().eq("account_id", accountId).prepare()
        );
    }

    private QueryBuilder<Coast, Integer> pageQuery(int page) throws SQLException {
        return queryBuilder().limit(limit).offset(page*limit);
    }
}