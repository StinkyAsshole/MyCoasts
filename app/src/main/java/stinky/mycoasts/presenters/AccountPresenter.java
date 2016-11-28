package stinky.mycoasts.presenters;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;

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
import stinky.mycoasts.model.entity.Category;
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

    private SubCategory getSubCategoryByNameAndCategory(Category category, String subcategoryName) throws SQLException {
        QueryBuilder<SubCategory, Integer> subCategoryQb = subCategoryRep.queryBuilder();
        QueryBuilder<Category, Integer> categoryQb = categoryRep.queryBuilder();
        categoryQb.where().eq(SubCategory.COLUMN_NAME, category.getName());
        subCategoryQb.join(categoryQb);
        subCategoryQb.where().eq(Category.COLUMN_NAME, subcategoryName);
        List<SubCategory> subCategories = subCategoryRep.query(subCategoryQb.prepare());
        if (subCategories == null || subCategories.isEmpty()){
            SubCategory subCategory = new SubCategory();
            subCategory.setName(subcategoryName);
            subCategory.setCategory(category);
            subCategoryRep.create(subCategory);
            return subCategory;
        }
        return subCategories.get(0);
    }

    public AccountPresenter() throws SQLException {
        accountRep = HelperFactory.getHelper().getAccountDao();
        coastRep = HelperFactory.getHelper().getCoastDao();
        categoryRep = HelperFactory.getHelper().getCategoryDao();
        subCategoryRep = HelperFactory.getHelper().getSubCategoryDao();
    }

    public void createAccount(String accName){
        if (accName == null || accName.isEmpty()){
            // TODO Заменить на ресурс
            getErrorView().showMessage("Введите название нового счета");
            return;
        }
        Account acc = new Account(accName);
        try {
            accountRep.create(acc);
        } catch (SQLException e) {
            getErrorView().onError(e);
        }

        getViewState().onCreateAccount(acc);
    }

    public void addCoast(String categoryName, String subcategoryName, int accountId, int amount){
        try {
            Category category = categoryRep.getByName(categoryName);
            SubCategory subCategory = getSubCategoryByNameAndCategory(category, subcategoryName);
            Account account = accountRep.queryForId(accountId);

            Coast coast = new Coast();
            coast.setAccount(account);
            coast.setAmount(amount);
            coast.setDate(DateUtils.now());
            coast.setSubCategory(subCategory);
            coastRep.create(coast);
            account.setAmount(account.getAmount() + amount);
            accountRep.update(account);
            getViewState().onAddCoast(coast);
        } catch (SQLException | NotFoundException e) {
            getErrorView().onError(e);
        }
    }

    public void selectAccount(Integer account) {
        Settings.setCurrentAccount(account);
        showCoastList(account, 0);
    }

//    public void showCoastListByCurrentMonth(Integer account) {
//        List<Coast> coastList;
//        try {
//            coastList = coastRep.getByDate(account, DateUtils.getStartOfMonth(1), DateUtils.getFinishOfMonth(12), 0);
//        } catch (SQLException e) {
//            getErrorView().onError(e);
//            return;
//        }
//
//        getViewState().showCoastList(coastList, false);
//    }

    public void showCoastList(Integer account, int monthDiff) {
        List<Coast> coastList;
        try {
            coastList = coastRep.getByMonthDiff(account, monthDiff);
        } catch (SQLException e) {
            getErrorView().onError(e);
            return;
        }

        getViewState().showCoastList(coastList, false);
    }

    public List<Account> getAccountList() {
        try {
            return accountRep.getAll();
        } catch (SQLException e) {
            getErrorView().onError(e);
        }
        return new ArrayList<>();
    }

    public List<Category> getCategoryList(){
        try {
            return categoryRep.queryForAll();
        } catch (SQLException e) {
            getErrorView().onError(e);
        }
        return new ArrayList<>();
    }
}
