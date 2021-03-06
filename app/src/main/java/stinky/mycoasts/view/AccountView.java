package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;

public interface AccountView extends MvpView {
    void onCreateAccount(Account account);
    void onSelectAccount(int account);
    void onAddCoast(Coast coast);
}
