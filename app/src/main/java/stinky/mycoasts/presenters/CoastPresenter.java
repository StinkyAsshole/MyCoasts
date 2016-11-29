package stinky.mycoasts.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.Settings;
import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.dao.CategoryDAO;
import stinky.mycoasts.model.dao.CoastDAO;
import stinky.mycoasts.model.dao.SubCategoryDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.SubCategory;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.view.AccountView;
import stinky.mycoasts.view.CoastView;

@InjectViewState
public class CoastPresenter extends ParentPresenter<CoastView>{

    AccountDAO accountRep;
    CoastDAO coastRep;
    CategoryDAO categoryRep;
    SubCategoryDAO subCategoryRep;


    public CoastPresenter() throws SQLException {
        accountRep = HelperFactory.getHelper().getAccountDao();
        coastRep = HelperFactory.getHelper().getCoastDao();
        categoryRep = HelperFactory.getHelper().getCategoryDao();
        subCategoryRep = HelperFactory.getHelper().getSubCategoryDao();
    }


    public void showCoastList(int accountId, int monthDif) {
        List<Coast> list = null;
        try {
            list = coastRep.getByMonthDiff(accountId, monthDif);
        } catch (SQLException e) {
            // TODO: Славик. обработать
        }
        getViewState().onShowCoastList(list);
    }
}
