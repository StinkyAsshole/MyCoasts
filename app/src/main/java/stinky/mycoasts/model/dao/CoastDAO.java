package stinky.mycoasts.model.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.tools.DateUtils;

public class CoastDAO extends BaseDaoImpl<Coast, Integer> {

    private long limit = 40;

    public CoastDAO(ConnectionSource connectionSource, Class<Coast> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

//    public List<Coast> getByDate(int accountId, Date startDate, Date finishDate, int page) throws SQLException{
//        return this.query(
//                pageQuery(page).where().ge("date", startDate).and().le("date", finishDate).and().eq("account_id", accountId).prepare()
//        );
//    }

    public List<Coast> getByMonthDiff(int accountId, int month) throws SQLException{
        int m = DateUtils.now().minusMonth(month).getMonth();
        return this.query(
                queryBuilder().orderBy("date", false).where().ge("date", DateUtils.getStartOfMonth(m)).and().le("date", DateUtils.getFinishOfMonth(m)).and().eq("account_id", accountId).prepare()
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
        return queryBuilder().limit(limit).offset(page*limit).orderBy("date", false);
    }

    public List<Coast> getByPage(int account, int page) throws SQLException {
        return this.query(pageQuery(page).where().eq("account_id", account).prepare());
    }

    public int deleteByAccountId(int accountid) throws SQLException {
        DeleteBuilder<Coast, Integer> deleteBuilder = deleteBuilder();
        deleteBuilder.where().eq("account_id", accountid);
        return deleteBuilder.delete();
    }
}