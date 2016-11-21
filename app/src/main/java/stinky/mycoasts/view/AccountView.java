package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;

public interface AccountView extends MvpView {
    void onCreateAccount(Account account);
    void showCoastList(List<Coast> coasts, boolean toStart);
    void onAddCoast(Coast coast);
}
