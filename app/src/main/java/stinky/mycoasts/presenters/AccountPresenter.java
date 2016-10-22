package stinky.mycoasts.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.sql.SQLException;

import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.view.AccountView;

@InjectViewState
public class AccountPresenter extends MvpPresenter<AccountView>{

    AccountDAO accountRep;

    public AccountPresenter() throws SQLException {
        accountRep = HelperFactory.getHelper().getAccountDao();
    }

    public void createAccount(String accName){
        Account acc = new Account(accName);
        try {
            accountRep.create(acc);
        } catch (SQLException e) {
            getViewState().onError(e);
        }

        getViewState().createAccount(acc);
    }

}
