package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;

public interface AccountView extends MvpView {
    void createAccount(Account account);
    void selectAccount(Account account, List<Coast> coasts);
    void onAddCoast(Coast coast);
}
