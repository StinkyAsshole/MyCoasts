package stinky.mycoasts.presenters;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.dao.CategoryDAO;
import stinky.mycoasts.model.dao.CoastDAO;
import stinky.mycoasts.model.dao.SubCategoryDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.view.AccountView;

@InjectViewState
public class AccountPresenter extends ParentPresenter<AccountView>{

    AccountDAO accountRep;
    CoastDAO coastRep;
    CategoryDAO categoryRep;
    SubCategoryDAO subCategoryRep;

    public AccountPresenter() throws SQLException {
        accountRep = HelperFactory.getHelper().getAccountDao();
        coastRep = HelperFactory.getHelper().getCoastDao();
        categoryRep = HelperFactory.getHelper().getCategoryDao();
        subCategoryRep = HelperFactory.getHelper().getSubCategoryDao();
    }

    public void createAccount(String accName){
        if (accName == null || accName.isEmpty()){
            Log.d("MainActivity", "asd");
            getErrorView().showMessage("Введите название нового счета");
            return;
        }
        Account acc = new Account(accName);
        try {
            accountRep.create(acc);
        } catch (SQLException e) {
            getErrorView().onError(e);
        }

        getViewState().createAccount(acc);
    }

    public void selectAccount(Account account) {
        List<Coast> coastList;
        try {
            coastList = coastRep.getByDate(account.getId(), DateUtils.getStartOfMonth(), DateUtils.getFinishOfMonth(), 0);
        } catch (SQLException e) {
            getErrorView().onError(e);
            return;
        }

        getViewState().selectAccount(account, coastList);
    }

    public List<Account> getAccountList() {
        try {
            return accountRep.getAll();
        } catch (SQLException e) {
            getErrorView().onError(e);
        }
        return new ArrayList<>();
    }
}
