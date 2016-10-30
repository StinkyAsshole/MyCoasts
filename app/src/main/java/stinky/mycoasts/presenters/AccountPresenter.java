package stinky.mycoasts.presenters;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.Dialogs;
import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.Settings;
import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.dao.CategoryDAO;
import stinky.mycoasts.model.dao.CoastDAO;
import stinky.mycoasts.model.dao.SubCategoryDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.SubCategory;
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

    public void addCoast(Integer subCategoryId, Integer amount){
        try {
            Account account = accountRep.queryForId(Settings.getCurrentAccount());
            SubCategory subCategory = subCategoryRep.queryForId(subCategoryId);
            Coast coast = new Coast();
            coast.setAccount(account);
            coast.setAmount(amount);
            coast.setDate(DateUtils.now());
            coast.setSubCategory(subCategory);
            coastRep.create(coast);
        } catch (SQLException | NotFoundException e) {
            getErrorView().onError(e);
        }
    }

    public void addCoastDialog(Context context){
        try {
            Dialogs.addCoast(context, categoryRep.getAll(), getErrorView(), new Dialogs.MyDialog.OnClickListener() {
                @Override
                public void onClick(Dialogs.MyDialog d) {

                }}, new Dialogs.MyDialog.OnClickListener() {
                @Override
                public void onClick(Dialogs.MyDialog d) {

                }
            }).show(Dialogs.Tags.ADD_COST);
        } catch (SQLException e) {
            getErrorView().onError(e);
        }
    }

    public void selectAccountById(Integer account) {
        try {
            Account a = accountRep.queryForId(account);
            selectAccount(a);
        } catch (SQLException e) {
            getErrorView().onError(e);
        }
    }
    public void selectAccount(Account account) {
        List<Coast> coastList;
        try {
            coastList = coastRep.getByDate(account.getId(), DateUtils.getStartOfMonth(1), DateUtils.getFinishOfMonth(12), 0);
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
